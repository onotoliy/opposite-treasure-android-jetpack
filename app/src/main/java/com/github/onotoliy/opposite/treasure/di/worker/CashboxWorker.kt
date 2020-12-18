package com.github.onotoliy.opposite.treasure.di.worker

import android.accounts.AccountManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.treasure.di.database.dao.CashboxDAO
import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO
import com.github.onotoliy.opposite.treasure.di.resource.CashboxResource
import com.github.onotoliy.opposite.treasure.utils.failure
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import com.github.onotoliy.opposite.treasure.utils.success
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
            builder.success()
        } else {
            builder.failure()
        }
    }

    private fun syncObject(builder: Data.Builder): Boolean {
        try {
            val response = retrofit.get("Bearer " + account.getAuthToken()).execute()
            val page = response.body()

            if (!response.isSuccessful || page == null) {
                if (response.code() == 504) {
                    return syncObject(builder)
                }

                return false
            }

            dao.replace(CashboxVO(deposit = page.deposit, lastUpdateDate = page.lastUpdateDate))

            return true
        } catch (exc: SocketTimeoutException) {
            return syncObject(builder)
        }
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