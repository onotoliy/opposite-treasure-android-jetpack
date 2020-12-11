package com.github.onotoliy.opposite.treasure.di.worker

import android.accounts.AccountManager
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.TransactionType.*
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.dao.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.database.data.toDTO
import com.github.onotoliy.opposite.treasure.di.database.data.toVO
import com.github.onotoliy.opposite.treasure.di.resource.TransactionResource
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Provider

class TransactionWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    dao: TransactionDAO,
    version: VersionDAO,
    private val retrofit: TransactionResource,
    account: AccountManager
) : AbstractPageWorker<Transaction, TransactionVO>(
    context,
    params,
    "transaction",
    version,
    dao,
    account
) {

    override fun toVO(dto: Transaction): TransactionVO = dto.toVO()

    override fun getRemoteVersion(): String = retrofit.version(account.getAuthToken()).name

    override fun getAll(token: String, version: Int, offset: Int, numberOfRows: Int): Call<Page<Transaction>> =
        retrofit.sync(token, version, offset, numberOfRows)

    override suspend fun doWork(): Result {
        dao.getAllLocal()
            .sortedByDescending {
                when (it.type) {
                    CONTRIBUTION -> 100
                    EARNED -> 90
                    PAID -> 80
                    WRITE_OFF -> 70
                    COST -> 60
                    NONE -> 50
                }
            }
            .forEach { vo ->
                val response = if (vo.updated == 1) {
                    retrofit.put(account.getAuthToken(), vo.toDTO()).execute()
                } else {
                    retrofit.post(account.getAuthToken(), vo.toDTO()).execute()
                }

                if (response.isSuccessful && response.body()?.uuid == vo.uuid) {
                    Log.i("TransactionWorker", "Success upload transaction: $vo")
                } else {
                    return Result.failure(
                        Data
                            .Builder()
                            .putString("uuid", vo.uuid)
                            .putBoolean("finished", true)
                            .putBoolean("success", false)
                            .build()
                    )
                }
            }

        return super.doWork()
    }

    class Factory @Inject constructor(
        private val dao: Provider<TransactionDAO>,
        private val version: Provider<VersionDAO>,
        private val retrofit: Provider<TransactionResource>,
        private val account: Provider<AccountManager>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker {
            return TransactionWorker(
                context,
                params,
                dao.get(),
                version.get(),
                retrofit.get(),
                account.get()
            )
        }
    }
}
