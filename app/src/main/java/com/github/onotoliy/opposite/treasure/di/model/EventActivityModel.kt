package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.utils.concat
import com.github.onotoliy.opposite.treasure.di.service.DebtService
import com.github.onotoliy.opposite.treasure.di.service.EventService
import com.github.onotoliy.opposite.treasure.di.service.TransactionService
import javax.inject.Inject

class EventActivityModel @Inject constructor(
    private val eventService: EventService,
    private val debtService: DebtService,
    private val transactionService: TransactionService
) {
    lateinit var pk: String
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val event: MutableLiveData<Event> = MutableLiveData()
    val debtors: MutableLiveData<Page<Deposit>> = MutableLiveData(Page())
    val transactions: MutableLiveData<Page<Transaction>> = MutableLiveData(Page())

    fun loading(pk: String) {
        this.pk = pk

        event.postValue(eventService.get(pk))

        nextDepositPageLoading()
        nextTransactionPageLoading()
    }

    fun nextDepositPageLoading(offset: Int = 0, numberOfRows: Int = 10) =
        debtService.getDebtorAll(pk, offset, numberOfRows).let {
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