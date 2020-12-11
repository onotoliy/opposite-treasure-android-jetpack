package com.github.onotoliy.opposite.treasure.di.worker

import android.accounts.AccountManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Debt
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.dao.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.DebtVO
import com.github.onotoliy.opposite.treasure.di.database.data.toVO
import com.github.onotoliy.opposite.treasure.di.resource.DebtResource
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Provider

class DebtWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    dao: DebtDAO,
    version: VersionDAO,
    private val retrofit: DebtResource,
    account: AccountManager
) : AbstractPageWorker<Debt, DebtVO>(context, params, "debt", version, dao, account) {
    override fun toVO(dto: Debt): DebtVO = dto.toVO()

    override fun getRemoteVersion(): String = retrofit.version(account.getAuthToken()).name

    override fun getAll(token: String, version: Int, offset: Int, numberOfRows: Int): Call<Page<Debt>> =
        retrofit.sync(token, version, offset, numberOfRows)

    override suspend fun doWork(): Result {
        dao.clean()

        return super.doWork()
    }

    class Factory @Inject constructor(
        private val dao: Provider<DebtDAO>,
        private val version: Provider<VersionDAO>,
        private val retrofit: Provider<DebtResource>,
        private val account: Provider<AccountManager>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker {
            return DebtWorker(
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
