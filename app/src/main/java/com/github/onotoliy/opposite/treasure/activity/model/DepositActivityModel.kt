package com.github.onotoliy.opposite.treasure.activity.model

import android.accounts.AccountManager
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.PageViewModel
import com.github.onotoliy.opposite.treasure.activity.EventPageCallback
import com.github.onotoliy.opposite.treasure.activity.TransactionPageCallback
import com.github.onotoliy.opposite.treasure.numberOfRows
import com.github.onotoliy.opposite.treasure.offset
import com.github.onotoliy.opposite.treasure.services.debts
import com.github.onotoliy.opposite.treasure.services.transactions

class DepositActivityModel(
    private val pk: String,
    private val manager: AccountManager,
    private val depositService: DepositService,
    private val cashboxService: CashboxService
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val cashbox: MutableLiveData<Cashbox> = MutableLiveData()
    val deposit: MutableLiveData<Deposit> = MutableLiveData()
    val events: MutableLiveData<PageViewModel<Event>> = MutableLiveData(PageViewModel())
    val transactions: MutableLiveData<PageViewModel<Transaction>> = MutableLiveData(PageViewModel())

    fun loading() {
        cashbox.postValue(cashboxService.get())
        deposit.postValue(depositService.get(pk))

        nextEventPageLoading()
        nextTransactionPageLoading()
    }

    fun nextEventPageLoading(offset: Int = 0, numberOfRows: Int = 10) = manager
        .debts
        .getAll(pk)
        .enqueue(EventPageCallback(events.value?.context ?: Page()) {
            events.postValue(
                PageViewModel(
                    offset = it.offset,
                    numberOfRows = it.numberOfRows,
                    context = it
                )
            )
        })

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) = manager
        .transactions
        .getAll(user = pk, offset = offset, numberOfRows = numberOfRows)
        .enqueue(TransactionPageCallback(transactions.value?.context ?: Page()) {
            transactions.postValue(
                PageViewModel(
                    offset = it.offset,
                    numberOfRows = it.numberOfRows,
                    context = it
                )
            )
        })
}
