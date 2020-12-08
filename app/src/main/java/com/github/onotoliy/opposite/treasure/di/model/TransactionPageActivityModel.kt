package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import com.github.onotoliy.opposite.treasure.di.database.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.TransactionVO
import com.github.onotoliy.opposite.treasure.di.service.toDTO
import javax.inject.Inject

class TransactionPageActivityModel @Inject constructor(
    private val dao: TransactionDAO
): ViewModel() {

    val pending: MutableLiveData<Boolean> = MutableLiveData(false)
    val page: MutableLiveData<Page<Transaction>> = MutableLiveData(Page())

    fun loading() {
        nextTransactionPageLoading()
    }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) {
        pending.postValue(true)

        dao.count().observeForever {
            val meta = Meta(it.toInt(), Paging(offset, numberOfRows))

            page.postValue(Page(meta, page.value?.context ?: listOf()))
        }

        dao.getAll(offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            page.postValue(Page(
                page.value?.meta ?: Meta(),
                mutableListOf<Transaction>().apply {
                    addAll(page.value?.context ?: listOf())
                    addAll(list.map(TransactionVO::toDTO))
                }
            ))
        }
    }
}
