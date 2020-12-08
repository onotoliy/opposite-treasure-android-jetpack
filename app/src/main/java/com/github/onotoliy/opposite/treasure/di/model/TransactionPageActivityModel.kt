package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.treasure.di.database.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.service.toDTO
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage
import javax.inject.Inject

class TransactionPageActivityModel @Inject constructor(
    private val dao: TransactionDAO
): ViewModel() {

    val pending: MutableLiveData<Boolean> = MutableLiveData(false)
    val page: LiveDataPage<Transaction> = LiveDataPage()

    fun loading() {
        dao.count().observeForever {
            page.total.postValue(it)
        }

        nextTransactionPageLoading()
    }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) {
        pending.postValue(true)

        dao.getAll(offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            page.offset = offset + numberOfRows
            page.numberOfRows = numberOfRows
            page.context.postValue(mutableListOf<Transaction>().apply{
                addAll(page.context.value ?: listOf())
                addAll(list.map { it.toDTO() })
            })
        }
    }
}
