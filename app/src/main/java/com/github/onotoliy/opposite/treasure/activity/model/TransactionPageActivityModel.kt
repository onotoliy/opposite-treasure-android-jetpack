package com.github.onotoliy.opposite.treasure.activity.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.treasure.PageViewModel
import com.github.onotoliy.opposite.treasure.numberOfRows
import com.github.onotoliy.opposite.treasure.offset

class TransactionPageActivityModel(
    private val transactionService: TransactionService
) {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: MutableLiveData<PageViewModel<Transaction>> = MutableLiveData(PageViewModel())

    fun loading() {
        nextTransactionPageLoading()
    }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) = transactionService
        .getAll(offset = offset, numberOfRows = numberOfRows)
        .let {
            val context = page.value?.context?.context?.toMutableList() ?: mutableListOf()

            context.addAll(it.context)

            pending.postValue(false)

            page.postValue(
                PageViewModel(
                    offset = it.offset,
                    numberOfRows = it.numberOfRows,
                    context = it
                )
            )
        }
}

