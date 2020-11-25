package com.github.onotoliy.opposite.treasure.works

import android.accounts.AccountManager
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.CashboxRepository
import com.github.onotoliy.opposite.treasure.di.database.DepositRepository
import com.github.onotoliy.opposite.treasure.di.database.EventRepository
import com.github.onotoliy.opposite.treasure.di.database.SQLiteDatabase
import com.github.onotoliy.opposite.treasure.resources.CashboxCallback
import com.github.onotoliy.opposite.treasure.resources.DefaultCallback
import com.github.onotoliy.opposite.treasure.services.cashbox
import com.github.onotoliy.opposite.treasure.services.deposits
import com.github.onotoliy.opposite.treasure.services.events

class DepositWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val deposit = DepositRepository(SQLiteDatabase(context))
        val cashbox = CashboxRepository(SQLiteDatabase(context))
        val event = EventRepository(SQLiteDatabase(context))
        val manager = AccountManager.get(context)

        manager.events.getAll(offset = 0, numberOfRows = 200).enqueue(
            DefaultCallback<Page<Event>>(
                onResponse = { page ->
                    page?.context?.forEach {
                        event.merge(it)
                    }
                }
            )
        )
        manager.deposits.getAll(0, 200).enqueue(
            DefaultCallback<Page<Deposit>>(
                onResponse = { page ->
                    page?.context?.forEach {
                        deposit.merge(it)
                    }
                }
            )
        )
        manager.cashbox.get().enqueue(CashboxCallback(
            onResponse = { value ->
                value?.let {
                    cashbox.merge(value)
                }
            }
        ))

        return Result.success()
    }
}