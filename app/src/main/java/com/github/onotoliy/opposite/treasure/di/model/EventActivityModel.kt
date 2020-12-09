package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.treasure.di.database.*
import com.github.onotoliy.opposite.treasure.di.service.toDTO
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage
import javax.inject.Inject

class EventActivityModel @Inject constructor(
    private val eventDAO: EventDAO,
    private val debtDAO: DebtDAO,
    private val transactionDAO: TransactionDAO
) {
    lateinit var pk: String
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val event: MutableLiveData<Event> = MutableLiveData()
    val debtors: LiveDataPage<Deposit> = LiveDataPage()
    val transactions: LiveDataPage<Transaction> = LiveDataPage()

    fun loading(pk: String) {
        this.pk = pk

        debtDAO.countByEvent(pk).observeForever {
            debtors.total.postValue(it)
        }
        transactionDAO.countByEvent(pk).observeForever {
            transactions.total.postValue(it)
        }
        eventDAO.get(pk).observeForever {
            event.postValue(it?.toDTO() ?: Event())
        }

        nextDepositPageLoading()
        nextTransactionPageLoading()
    }

    fun nextDepositPageLoading(offset: Int = 0, numberOfRows: Int = 10) {
        pending.postValue(true)

        debtDAO.getByEventAll(pk, offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            debtors.offset = offset + numberOfRows
            debtors.numberOfRows = numberOfRows
            debtors.context.postValue(mutableListOf<Deposit>().apply{
                addAll(debtors.context.value ?: listOf())
                addAll(list.map { it.toDTO() }.map { it.deposit })
            })
        }
    }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10){
        pending.postValue(true)

        transactionDAO.getByEventAll(pk, offset, numberOfRows).observeForever { list ->
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