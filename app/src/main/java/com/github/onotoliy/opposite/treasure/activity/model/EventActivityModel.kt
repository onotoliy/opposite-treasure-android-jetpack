package com.github.onotoliy.opposite.treasure.activity.model

import android.accounts.AccountManager
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.PageView
import com.github.onotoliy.opposite.treasure.PageViewModel
import com.github.onotoliy.opposite.treasure.activity.DepositPageCallback
import com.github.onotoliy.opposite.treasure.activity.TransactionPageCallback
import com.github.onotoliy.opposite.treasure.numberOfRows
import com.github.onotoliy.opposite.treasure.offset
import com.github.onotoliy.opposite.treasure.resources.EventCallback
import com.github.onotoliy.opposite.treasure.services.deposits
import com.github.onotoliy.opposite.treasure.services.events
import com.github.onotoliy.opposite.treasure.services.transactions

class EventActivityModel(
    private val pk: String,
    private val manager: AccountManager
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val event: MutableLiveData<Event> = MutableLiveData()
    val debtors: MutableLiveData<PageViewModel<Deposit>> = MutableLiveData(PageViewModel())
    val transactions: MutableLiveData<PageViewModel<Transaction>> = MutableLiveData(PageViewModel())

    fun loading() {
        manager.events.get(pk).enqueue(EventCallback {
            event.postValue(it)
        })

        nextDepositPageLoading()
        nextTransactionPageLoading()
    }

    fun nextDepositPageLoading(offset: Int = 0, numberOfRows: Int = 10) =  manager
        .deposits
        .getAll(offset = offset, numberOfRows = numberOfRows)
        .enqueue(DepositPageCallback(debtors.value?.context ?: Page()) {
            debtors.postValue(
                PageViewModel(
                    offset = it.offset,
                    numberOfRows = it.numberOfRows,
                    context = it
                )
            )
        })

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) =  manager
        .transactions
        .getAll(event = pk, offset = offset, numberOfRows = numberOfRows)
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