package com.github.onotoliy.opposite.treasure.di.worker

import android.accounts.AccountManager
import android.content.Context
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.WriteDAO
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Call
import retrofit2.Response
import java.net.SocketTimeoutException

abstract class AbstractPageWorker<D, E> constructor(
    context: Context,
    params: WorkerParameters,
    type: String,
    version: VersionDAO,
    protected val dao: WriteDAO<E>,
    protected val account: AccountManager
) : AbstractWorker<Page<D>, E>(context, params, type, dao, version) {

    protected abstract fun toVO(dto: D): E
    protected abstract fun getAll(
        token: String, version: Int, offset: Int, numberOfRows: Int
    ): Call<Page<D>>

    protected fun getAll(version: Int, offset: Int, numberOfRows: Int): Call<Page<D>> =
        getAll(
            token = account.getAuthToken(),
            version = version,
            offset = offset,
            numberOfRows = numberOfRows
        )

    override suspend fun sync(builder: Data.Builder, version: String): Boolean =
        sync(builder, version.toInt(), 0, 20)

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun sync(
        builder: Data.Builder, version: Int, offset: Int, numberOfRows: Int
    ): Boolean {
        try {
            val response: Response<Page<D>> = getAll(version, offset, numberOfRows).execute()
            val page = response.body()

            if (!response.isSuccessful || page == null) {
                if (response.code() == 504) {
                    return sync(builder, version, offset, numberOfRows)
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
                sync(builder, version, offset + numberOfRows, numberOfRows)
            }
        } catch (exc: SocketTimeoutException) {
            return sync(builder, version, offset, numberOfRows)
        }
    }
}
