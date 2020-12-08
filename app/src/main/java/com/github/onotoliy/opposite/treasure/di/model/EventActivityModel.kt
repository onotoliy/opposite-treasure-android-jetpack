package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Debt
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import com.github.onotoliy.opposite.treasure.di.database.*
import com.github.onotoliy.opposite.treasure.di.service.toDTO
import javax.inject.Inject

class EventActivityModel @Inject constructor(
    private val eventDAO: EventDAO,
    private val debtDAO: DebtDAO,
    private val transactionDAO: TransactionDAO
) {
    lateinit var pk: String
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val event: MutableLiveData<Event> = MutableLiveData()
    val debtors: MutableLiveData<Page<Deposit>> = MutableLiveData(Page())
    val transactions: MutableLiveData<Page<Transaction>> = MutableLiveData(Page())

    fun loading(pk: String) {
        this.pk = pk

        eventDAO.get(pk).observeForever {
            event.postValue(it?.toDTO() ?: Event())
        }

        nextDepositPageLoading()
        nextTransactionPageLoading()
    }

    fun nextDepositPageLoading(offset: Int = 0, numberOfRows: Int = 10) {
        pending.postValue(true)

        this.debtDAO.countByEvent(pk).observeForever {
            val meta = Meta(it.toInt(), Paging(offset, numberOfRows))

            debtors.postValue(Page(meta, debtors.value?.context ?: listOf()))
        }

        this.debtDAO.getByEventAll(pk, offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            debtors.postValue(Page(
                debtors.value?.meta ?: Meta(),
                mutableListOf<Deposit>().apply {
                    addAll(debtors.value?.context ?: listOf())
                    addAll(list.map(DebtVO::toDTO).map(Debt::deposit))
                }
            ))
        }
    }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10){
        pending.postValue(true)

        this.transactionDAO.countByEvent(pk).observeForever {
            val meta = Meta(it.toInt(), Paging(offset, numberOfRows))

            transactions.postValue(Page(meta, transactions.value?.context ?: listOf()))
        }

        this.transactionDAO.getByEventAll(pk, offset, numberOfRows).observeForever { list ->
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