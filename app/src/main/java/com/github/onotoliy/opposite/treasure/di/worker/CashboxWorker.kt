package com.github.onotoliy.opposite.treasure.di.worker

import android.accounts.AccountManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.treasure.di.database.dao.CashboxDAO
import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO
import com.github.onotoliy.opposite.treasure.di.resource.CashboxResource
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Provider

class CashboxWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    private val dao: CashboxDAO,
    private val retrofit: CashboxResource,
    private val account: AccountManager
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val builder: Data.Builder = Data.Builder()

        return if (syncObject(builder)) {
            Result.success(builder.putBoolean("success", true).putBoolean("finished" , true).build())
        } else {
            Result.failure(builder.putBoolean("success", false).putBoolean("finished" , true).build())
        }
    }

    private fun syncObject(
        builder: Data.Builder
    ): Boolean {
        val response: Response<Cashbox>

        try {
            response = retrofit.get("Bearer " + account.getAuthToken()).execute()
        } catch (exc: SocketTimeoutException) {
            return syncObject(builder)
        }

        val page = response.body()

        if (!response.isSuccessful || page == null) {
            if (response.code() == 504) {
                return syncObject(builder)
            }
            return false
        }

        dao.replace(CashboxVO(deposit = page.deposit, lastUpdateDate = page.lastUpdateDate))

        return true
    }

    class Factory @Inject constructor(
        private val dao: Provider<CashboxDAO>,
        private val retrofit: Provider<CashboxResource>,
        private val account: Provider<AccountManager>
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker {
            return CashboxWorker(
                context,
                params,
                dao.get(),
                retrofit.get(),
                account.get()
            )
        }
    }
}