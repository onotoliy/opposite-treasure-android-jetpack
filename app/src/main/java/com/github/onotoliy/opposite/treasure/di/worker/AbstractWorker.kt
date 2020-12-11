package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.WriteDAO

abstract class AbstractWorker<D, E> constructor(
    context: Context,
    params: WorkerParameters,
    private val type: String,
    private val dao: WriteDAO<E>,
    private val version: VersionDAO
) : CoroutineWorker(context, params) {

    protected abstract fun getRemoteVersion(): String

    protected abstract suspend fun sync(
        builder: Data.Builder, version: String
    ): Boolean

    override suspend fun doWork(): Result {
        val localVersion = version.get(type).version
        val remoteVersion = getRemoteVersion()

        val builder: Data.Builder = Data.Builder()
            .putString("local-version", localVersion)
            .putString("local-remote", remoteVersion)

        if (localVersion == remoteVersion) {
            return Result.success(
                builder.putBoolean("success", true).build()
            )
        }

        return if (sync(builder, localVersion)) {
            Result.success(
                builder.putBoolean("success", true).putBoolean("finished", true).build()
            )
        } else {
            Result.failure(
                builder.putBoolean("success", false).putBoolean("finished", true).build()
            )
        }
    }
}
