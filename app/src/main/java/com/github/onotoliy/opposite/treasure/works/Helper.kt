package com.github.onotoliy.opposite.treasure.works

import android.accounts.AccountManager
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.database.CashboxHelper
import com.github.onotoliy.opposite.treasure.database.DepositHelper
import com.github.onotoliy.opposite.treasure.database.EventHelper
import com.github.onotoliy.opposite.treasure.database.SQLiteHelper
import com.github.onotoliy.opposite.treasure.resources.CashboxCallback
import com.github.onotoliy.opposite.treasure.resources.DefaultCallback
import com.github.onotoliy.opposite.treasure.services.cashbox
import com.github.onotoliy.opposite.treasure.services.deposits
import com.github.onotoliy.opposite.treasure.services.events

class DepositWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val deposit = DepositHelper(SQLiteHelper(context))
        val cashbox = CashboxHelper(SQLiteHelper(context))
        val event = EventHelper(SQLiteHelper(context))
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