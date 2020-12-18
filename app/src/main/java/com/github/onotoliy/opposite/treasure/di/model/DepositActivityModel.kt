package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.treasure.di.database.dao.CashboxDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage
import javax.inject.Inject

class DepositActivityModel @Inject constructor(
    private val depositDAO: DepositDAO,
    private val cashboxDAO: CashboxDAO,
    private val debtDAO: DebtDAO,
    private val transactionDAO: TransactionRepository,
) {
    lateinit var pk: String

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val cashbox: MutableLiveData<CashboxVO> = MutableLiveData()
    val deposit: MutableLiveData<DepositVO> = MutableLiveData()
    val debts: LiveDataPage<EventVO> = LiveDataPage()
    val transactions: LiveDataPage<TransactionVO> = LiveDataPage()

    fun loading(pk: String) {
        this.pk = pk

        debtDAO.countByPerson(pk).observeForever {
            debts.total.postValue(it)
        }
        transactionDAO.countByPerson(pk).observeForever {
            transactions.total.postValue(it)
        }
        cashboxDAO.get().observeForever {
            cashbox.postValue(it)
        }
        depositDAO.get(pk).observeForever {
            deposit.postValue(it)
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
            debts.context.postValue(mutableListOf<EventVO>().apply{
                addAll(debts.context.value ?: listOf())
                addAll(list.map { it.event })
            })
        }
    }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) {
        transactionDAO.getByPersonAll(pk, offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            transactions.offset = offset + numberOfRows
            transactions.numberOfRows = numberOfRows
            transactions.context.postValue(mutableListOf<TransactionVO>().apply{
                addAll(transactions.context.value ?: listOf())
                addAll(list)
            })
        }
    }
}
