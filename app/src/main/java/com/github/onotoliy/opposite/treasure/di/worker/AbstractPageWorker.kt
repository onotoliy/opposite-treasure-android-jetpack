package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.core.HasUUID
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.dao.WriteDAO
import com.github.onotoliy.opposite.treasure.di.database.repositories.AbstractRepository
import com.github.onotoliy.opposite.treasure.di.restful.resource.Resource
import com.github.onotoliy.opposite.treasure.utils.failure
import com.github.onotoliy.opposite.treasure.utils.progress
import com.github.onotoliy.opposite.treasure.utils.setLocalVersion
import com.github.onotoliy.opposite.treasure.utils.setWorker
import com.github.onotoliy.opposite.treasure.utils.setRemoteVersion
import com.github.onotoliy.opposite.treasure.utils.success
import retrofit2.Response
import java.net.SocketTimeoutException

abstract class AbstractPageWorker<D, E: HasUUID, DAO: WriteDAO<E>> constructor(
    context: Context,
    params: WorkerParameters,
    protected val repository: AbstractRepository<E, DAO>,
    protected val resource: Resource<D>
) : CoroutineWorker(context, params) {

    protected abstract fun toVO(dto: D): E

    protected open fun sendAllLocal(builder: Data.Builder): Boolean = true

    override suspend fun doWork(): Result {
        val builder: Data.Builder = Data.Builder()
            .setWorker(this.javaClass.simpleName)

        if (!sendAllLocal(builder)) {
            return builder.failure()
        }

        val localVersion = repository.getVersion()
        val remoteVersion = resource.getVersion()

        builder
            .setLocalVersion(localVersion)
            .setRemoteVersion(remoteVersion)

        Log.i(this.javaClass.simpleName, "LocalVersion: $localVersion. RemoteVersion $remoteVersion")

        return if (localVersion == remoteVersion) {
            builder.success()
        } else {
            repository.clean()

            if (sync(builder, 0L, 0, 20)) {
                repository.setVersion(remoteVersion)

                builder.success()
            } else {
                builder.failure()
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun sync(
        builder: Data.Builder, version: Long, offset: Int, numberOfRows: Int
    ): Boolean {
        return try {
            val response: Response<Page<D>> = resource.getAll(version, offset, numberOfRows)
            val page = response.body()

            if (!response.isSuccessful || page == null) {
                if (response.code() == 504) {
                    sync(builder, version, offset, numberOfRows)
                } else {
                    false
                }
            } else {
                setProgress(
                    progress(
                        worker = this.javaClass.simpleName,
                        total = page.meta.total,
                        offset = offset + numberOfRows
                    )
                )

                page.context.map(this::toVO).forEach(repository::replace)

                if (offset > page.meta.total) {
                    true
                } else {
                    sync(builder, version, offset + numberOfRows, numberOfRows)
                }
            }
        } catch (exc: SocketTimeoutException) {
            sync(builder, version, offset, numberOfRows)
        }
    }
}
