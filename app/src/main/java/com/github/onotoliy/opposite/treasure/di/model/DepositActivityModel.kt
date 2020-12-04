package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.CashboxVO
import com.github.onotoliy.opposite.treasure.di.service.*
import com.github.onotoliy.opposite.treasure.utils.concat
import javax.inject.Inject

class DepositActivityModel @Inject constructor(
    private val depositService: DepositService,
    private val cashboxService: CashboxService,
    private val debtService: DebtService,
    private val transactionService: TransactionService,
) {
    lateinit var pk: String

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val cashbox: MutableLiveData<Cashbox> = MutableLiveData()
    val deposit: MutableLiveData<Deposit> = MutableLiveData()
    val events: MutableLiveData<Page<Event>> = MutableLiveData(Page())
    val transactions: MutableLiveData<Page<Transaction>> = MutableLiveData(Page())

    fun loading(pk: String) {
        this.pk = pk

        cashboxService.get().observeForever {
            cashbox.postValue(it.toDTO())
        }
        depositService.get(pk).observeForever {
            deposit.postValue(it.toDTO())
        }

        nextEventPageLoading()
        nextTransactionPageLoading()
    }

    fun nextEventPageLoading(offset: Int = 0, numberOfRows: Int = 10) {
        debtService
        .getDebtAll(deposit = pk, offset = offset, numberOfRows = numberOfRows)
        .content.observeForever { other ->
            pending.postValue(false)
            events.postValue(events.value.concat(other.map { it.toDTO().event }))
        }
        }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) = transactionService
        .getAll(person = pk, offset = offset, numberOfRows = numberOfRows)
        .let {
            pending.postValue(false)
            transactions.postValue(transactions.value.concat(it))
        }
}
