package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.*
import com.github.onotoliy.opposite.treasure.di.database.*
import com.github.onotoliy.opposite.treasure.di.service.*
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage
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
    val debts: LiveDataPage<Event> = LiveDataPage()
    val transactions: LiveDataPage<Transaction> = LiveDataPage()

    fun loading(pk: String) {
        this.pk = pk

        debtDAO.countByPerson(pk).observeForever {
            debts.total.postValue(it)
        }
        transactionDAO.countByPerson(pk).observeForever {
            transactions.total.postValue(it)
        }
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
        pending.postValue(true)

        debtDAO.getByPersonAll(pk, offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            debts.offset = offset + numberOfRows
            debts.numberOfRows = numberOfRows
            debts.context.postValue(mutableListOf<Event>().apply{
                addAll(debts.context.value ?: listOf())
                addAll(list.map { it.toDTO() }.map { it.event })
            })
        }
    }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) {
        transactionDAO.getByPersonAll(pk, offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            transactions.offset = offset + numberOfRows
            transactions.numberOfRows = numberOfRows
            transactions.context.postValue(mutableListOf<Transaction>().apply{
                addAll(transactions.context.value ?: listOf())
                addAll(list.map { it.toDTO() })
            })
        }
    }
}
