package com.github.onotoliy.opposite.treasure.di.worker

import android.accounts.AccountManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.dao.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.data.toVO
import com.github.onotoliy.opposite.treasure.di.resource.DepositResource
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Provider

class DepositWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    dao: DepositDAO,
    version: VersionDAO,
    private val retrofit: DepositResource,
    account: AccountManager
) : AbstractPageWorker<Deposit, DepositVO>(context, params, "deposit", version, dao, account) {
    override fun toVO(dto: Deposit): DepositVO = dto.toVO()

    override fun getRemoteVersion(): String = retrofit.version(account.getAuthToken()).name

    override fun getAll(token: String, version: Int, offset: Int, numberOfRows: Int): Call<Page<Deposit>> =
        retrofit.sync(token, version, offset, numberOfRows)

    class Factory @Inject constructor(
        private val dao: Provider<DepositDAO>,
        private val version: Provider<VersionDAO>,
        private val retrofit: Provider<DepositResource>,
        private val account: Provider<AccountManager>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker {
            return DepositWorker(
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
