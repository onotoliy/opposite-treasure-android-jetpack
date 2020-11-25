package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.concat
import com.github.onotoliy.opposite.treasure.di.service.CashboxService
import com.github.onotoliy.opposite.treasure.di.service.DebtService
import com.github.onotoliy.opposite.treasure.di.service.DepositService
import com.github.onotoliy.opposite.treasure.di.service.TransactionService

class DepositActivityModel(
    private val pk: String,
    private val depositService: DepositService,
    private val cashboxService: CashboxService,
    private val transactionService: TransactionService,
    private val debtService: DebtService,
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val cashbox: MutableLiveData<Cashbox> = MutableLiveData()
    val deposit: MutableLiveData<Deposit> = MutableLiveData()
    val events: MutableLiveData<Page<Event>> = MutableLiveData(Page())
    val transactions: MutableLiveData<Page<Transaction>> = MutableLiveData(Page())

    fun loading() {
        cashbox.postValue(cashboxService.get())
        deposit.postValue(depositService.get(pk))

        nextEventPageLoading()
        nextTransactionPageLoading()
    }

    fun nextEventPageLoading(offset: Int = 0, numberOfRows: Int = 10) = debtService
        .getAll(person = pk, offset = offset, numberOfRows = numberOfRows)
        .let {
            pending.postValue(false)
            events.postValue(events.value.concat(it))
        }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) = transactionService
        .getAll(person = pk, offset = offset, numberOfRows = numberOfRows)
        .let {
            pending.postValue(false)
            transactions.postValue(transactions.value.concat(it))
        }
}
