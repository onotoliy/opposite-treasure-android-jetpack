package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.*
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import com.github.onotoliy.opposite.treasure.di.database.*
import com.github.onotoliy.opposite.treasure.di.service.*
import javax.inject.Inject

class DepositActivityModel @Inject constructor(
    private val depositDAO: DepositDAO,
    private val cashboxDAO: CashboxDAO,
    private val debtDAO: DebtDAO,
    private val transactionDAO: TransactionDAO,
) {
    lateinit var pk: String

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val cashbox: MutableLiveData<Cashbox> = MutableLiveData()
    val deposit: MutableLiveData<Deposit> = MutableLiveData()
    val events: MutableLiveData<Page<Event>> = MutableLiveData(Page())
    val transactions: MutableLiveData<Page<Transaction>> = MutableLiveData(Page())

    fun loading(pk: String) {
        this.pk = pk

        cashboxDAO.get().observeForever {
            cashbox.postValue(it.toDTO())
        }
        depositDAO.get(pk).observeForever {
            deposit.postValue(it?.toDTO() ?: Deposit())
        }

        nextEventPageLoading()
        nextTransactionPageLoading()
    }

    fun nextEventPageLoading(offset: Int = 0, numberOfRows: Int = 10) {
        this.debtDAO.countByPerson(pk).observeForever {
            val meta = Meta(it.toInt(), Paging(offset, numberOfRows))

            events.postValue(Page(meta, events.value?.context ?: listOf()))
        }

        this.debtDAO.getByPersonAll(pk, offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            events.postValue(Page(
                events.value?.meta ?: Meta(),
                mutableListOf<Event>().apply {
                    addAll(events.value?.context ?: listOf())
                    addAll(list.map(DebtVO::toDTO).map(Debt::event))
                }
            ))
        }
    }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) {
        this.transactionDAO.countByPerson(pk).observeForever {
            val meta = Meta(it.toInt(), Paging(offset, numberOfRows))

            transactions.postValue(Page(meta, transactions.value?.context ?: listOf()))
        }

        this.transactionDAO.getByPersonAll(pk, offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            transactions.postValue(Page(
                transactions.value?.meta ?: Meta(),
                mutableListOf<Transaction>().apply {
                    addAll(transactions.value?.context ?: listOf())
                    addAll(list.map(TransactionVO::toDTO))
                }
            ))
        }
    }
}
