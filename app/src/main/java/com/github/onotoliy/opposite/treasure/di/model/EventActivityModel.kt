package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.treasure.PageViewModel
import com.github.onotoliy.opposite.treasure.di.service.DepositService
import com.github.onotoliy.opposite.treasure.di.service.EventService
import com.github.onotoliy.opposite.treasure.di.service.TransactionService
import com.github.onotoliy.opposite.treasure.numberOfRows
import com.github.onotoliy.opposite.treasure.offset

class EventActivityModel(
    private val pk: String,
    private val depositService: DepositService,
    private val eventService: EventService,
    private val transactionService: TransactionService
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val event: MutableLiveData<Event> = MutableLiveData()
    val debtors: MutableLiveData<PageViewModel<Deposit>> = MutableLiveData(PageViewModel())
    val transactions: MutableLiveData<PageViewModel<Transaction>> = MutableLiveData(PageViewModel())

    fun loading() {
        event.postValue(eventService.get(pk))

        nextDepositPageLoading()
        nextTransactionPageLoading()
    }

    fun nextDepositPageLoading(offset: Int = 0, numberOfRows: Int = 10) =
        depositService.getAll(offset, numberOfRows).let {
            val context = debtors.value?.context?.context?.toMutableList() ?: mutableListOf()

            context.addAll(it.context)

            pending.postValue(false)

            debtors.postValue(
                PageViewModel(
                    offset = it.offset,
                    numberOfRows = it.numberOfRows,
                    context = it
                )
            )
        }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) = transactionService
        .getAll(event = pk, offset = offset, numberOfRows = numberOfRows)
        .let {
            val context = transactions.value?.context?.context?.toMutableList() ?: mutableListOf()

            context.addAll(it.context)

            pending.postValue(false)

            transactions.postValue(
                PageViewModel(
                    offset = it.offset,
                    numberOfRows = it.numberOfRows,
                    context = it
                )
            )
        }

}