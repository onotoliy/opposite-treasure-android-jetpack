package com.github.onotoliy.opposite.treasure.di.worker

import android.accounts.AccountManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.dao.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
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
    private val account: AccountManager
) : AbstractPageWorker<Transaction, TransactionVO>(context, params, dao, version) {

    override fun toVO(dto: Transaction): TransactionVO = dto.toVO()

    override fun getVersion(): Int = 1

    override fun sync(version: Int, offset: Int, numberOfRows: Int): Call<Page<Transaction>> =
        retrofit.sync("Bearer " + account.getAuthToken(), version, offset, numberOfRows)

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
