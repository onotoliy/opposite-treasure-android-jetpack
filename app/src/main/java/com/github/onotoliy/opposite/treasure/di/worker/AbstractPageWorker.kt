package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.core.HasUUID
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.dao.WriteDAO
import com.github.onotoliy.opposite.treasure.di.database.repositories.AbstractRepository
import com.github.onotoliy.opposite.treasure.di.resource.Retrofit
import com.github.onotoliy.opposite.treasure.utils.*
import retrofit2.Response
import java.net.SocketTimeoutException

abstract class AbstractPageWorker<D, E: HasUUID, DAO: WriteDAO<E>> constructor(
    context: Context,
    params: WorkerParameters,
    protected val repository: AbstractRepository<E, DAO>,
    protected val retrofit: Retrofit<D>
) : CoroutineWorker(context, params) {

    protected abstract fun toVO(dto: D): E

    protected open fun sendAllLocal(builder: Data.Builder): Boolean = true

    override suspend fun doWork(): Result {
        val localVersion = repository.getVersion()
        val remoteVersion = retrofit.getVersion()

        val builder: Data.Builder = Data.Builder()
            .setLocalVersion(localVersion)
            .setRemoteVersion(remoteVersion)

        if (!sendAllLocal(builder)) {
            return builder.failure()
        }

        if (localVersion == remoteVersion) {
            return builder.success()
        }

        return if (sync(builder, localVersion.toLong(), 0, 20)) {
            repository.setVersion(remoteVersion)

            builder.success()
        } else {
            builder.failure()
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun sync(
        builder: Data.Builder, version: Long, offset: Int, numberOfRows: Int
    ): Boolean {
        try {
            val response: Response<Page<D>> = retrofit.getAll(version, offset, numberOfRows)
            val page = response.body()

            if (!response.isSuccessful || page == null) {
                if (response.code() == 504) {
                    return sync(builder, version, offset, numberOfRows)
                }

                return false
            }

            setProgress(progress(
                total = page.meta.total,
                offset = offset + numberOfRows
            ))

            page.context.map(this::toVO).forEach(repository::replace)

            return if (offset > page.meta.total) {
                return true
            } else {
                sync(builder, version, offset + numberOfRows, numberOfRows)
            }
        } catch (exc: SocketTimeoutException) {
            return sync(builder, version, offset, numberOfRows)
        }
    }
}
