package com.github.onotoliy.opposite.treasure.di.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.treasure.di.service.*
import javax.inject.Inject

class SyncWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    @Inject lateinit var debt: DebtService
    @Inject lateinit var event: EventService
    @Inject lateinit var deposit: DepositService
    @Inject lateinit var cashbox: CashboxService
    @Inject lateinit var transaction: TransactionService

    override fun doWork(): Result {
        debt.sync()
        event.sync()
        deposit.sync()
        cashbox.sync()
        transaction.sync()

        return Result.success()
    }
}