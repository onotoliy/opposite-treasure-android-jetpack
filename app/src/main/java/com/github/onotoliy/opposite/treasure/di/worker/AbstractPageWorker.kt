package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.WriteDAO
import com.github.onotoliy.opposite.treasure.di.database.data.VersionVO
import retrofit2.Call
import retrofit2.Response
import java.net.SocketTimeoutException

abstract class AbstractPageWorker<D, E> constructor(
    context: Context,
    params: WorkerParameters,
    private val dao: WriteDAO<E>,
    private val version: VersionDAO
) : CoroutineWorker(context, params) {

    protected abstract fun toVO(dto: D): E
    protected abstract fun getVersion(): Int
    protected abstract fun sync(version: Int, offset: Int, numberOfRows: Int): Call<Page<D>>

    override suspend fun doWork(): Result {
        val localVersion = VersionVO("0", "0").version
        val remoteVersion = getVersion().toString()

        val builder: Data.Builder = Data.Builder()
            .putString("local-version", localVersion)
            .putString("local-remote", remoteVersion)

        if (localVersion == remoteVersion) {
            return Result.success(
                builder.putBoolean("success", true).build()
            )
        }

        return if (syncPage(builder, localVersion.toInt())) {
            Result.success(builder.putBoolean("success", true).putBoolean("finished", true).build())
        } else {
            Result.failure(
                builder.putBoolean("success", false).putBoolean("finished", true).build()
            )
        }
    }

    private suspend fun syncPage(
        builder: Data.Builder,
        version: Int,
        offset: Int = 0,
        numberOfRows: Int = 20
    ): Boolean {
        val response: Response<Page<D>>

        try {
            response = sync(version, offset, numberOfRows).execute()
        } catch (exc: SocketTimeoutException) {
            return syncPage(builder, version, offset, numberOfRows)
        }

        val page = response.body()

        if (!response.isSuccessful || page == null) {
            if (response.code() == 504) {
                return syncPage(builder, version, offset, numberOfRows)
            }
            return false
        }

        setProgress(
            Data.Builder()
                .putInt("total", page.meta.total)
                .putInt("offset", offset + numberOfRows)
                .build()
        )

        page.context.map(this::toVO).forEach(dao::replace)

        return if (offset > page.meta.total) {
            return true
        } else {
            syncPage(builder, version, offset + numberOfRows, numberOfRows)
        }
    }
}