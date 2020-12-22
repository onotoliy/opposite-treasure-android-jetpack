package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.treasure.di.database.dao.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.EventRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage
import javax.inject.Inject

class EventActivityModel @Inject constructor(
    private val eventDAO: EventRepository,
    private val debtDAO: DebtDAO,
    private val transactionDAO: TransactionRepository
) {
    lateinit var pk: String
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val event: MutableLiveData<EventVO> = MutableLiveData()
    val debtors: LiveDataPage<DepositVO> = LiveDataPage()
    val transactions: LiveDataPage<TransactionVO> = LiveDataPage()

    fun loading(pk: String) {
        this.pk = pk

        debtDAO.countByEvent(pk).observeForever {
            debtors.total.postValue(it)
        }
        transactionDAO.countByEvent(pk).observeForever {
            transactions.total.postValue(it)
        }
        eventDAO.get(pk).observeForever {
            event.postValue(it)
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
            debtors.context.postValue(mutableListOf<DepositVO>().apply{
                addAll(debtors.context.value ?: listOf())
                addAll(list.map { it.deposit })
            })
        }
    }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10){
        pending.postValue(true)

        transactionDAO.getByEventAll(pk, offset, numberOfRows).observeForever { list ->
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
