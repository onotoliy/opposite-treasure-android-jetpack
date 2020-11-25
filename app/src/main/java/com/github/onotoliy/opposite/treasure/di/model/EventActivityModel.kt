package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.concat
import com.github.onotoliy.opposite.treasure.di.service.DebtorService
import com.github.onotoliy.opposite.treasure.di.service.EventService
import com.github.onotoliy.opposite.treasure.di.service.TransactionService

class EventActivityModel(
    private val pk: String,
    private val eventService: EventService,
    private val transactionService: TransactionService,
    private val debtorService: DebtorService
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val event: MutableLiveData<Event> = MutableLiveData()
    val debtors: MutableLiveData<Page<Deposit>> = MutableLiveData(Page())
    val transactions: MutableLiveData<Page<Transaction>> = MutableLiveData(Page())

    fun loading() {
        event.postValue(eventService.get(pk))

        nextDepositPageLoading()
        nextTransactionPageLoading()
    }

    fun nextDepositPageLoading(offset: Int = 0, numberOfRows: Int = 10) =
        debtorService.getAll(pk, offset, numberOfRows).let {
            pending.postValue(false)
            debtors.postValue(debtors.value.concat(it))
        }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) = transactionService
        .getAll(event = pk, offset = offset, numberOfRows = numberOfRows)
        .let {
            pending.postValue(false)
            transactions.postValue(transactions.value.concat(it))
        }

}