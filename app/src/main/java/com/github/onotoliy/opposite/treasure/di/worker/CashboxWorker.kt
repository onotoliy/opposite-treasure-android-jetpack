package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.treasure.di.database.repositories.CashboxRepository
import com.github.onotoliy.opposite.treasure.di.resource.CashboxRetrofit
import com.github.onotoliy.opposite.treasure.utils.failure
import com.github.onotoliy.opposite.treasure.utils.success
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Provider

class CashboxWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    private val repository: CashboxRepository,
    private val retrofit: CashboxRetrofit
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
            val response = retrofit.get()
            val page = response.body()

            if (!response.isSuccessful || page == null) {
                if (response.code() == 504) {
                    return syncObject(builder)
                }

                return false
            }

            repository.replace(deposit = page.deposit, lastUpdateDate = page.lastUpdateDate)

            return true
        } catch (exc: SocketTimeoutException) {
            return syncObject(builder)
        }
    }

    class Factory @Inject constructor(
        private val dao: Provider<CashboxRepository>,
        private val retrofit: Provider<CashboxRetrofit>,
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): CoroutineWorker =
            CashboxWorker(context, params, dao.get(), retrofit.get())
    }
}