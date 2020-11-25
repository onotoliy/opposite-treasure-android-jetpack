package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.concat
import com.github.onotoliy.opposite.treasure.di.service.TransactionService
import javax.inject.Inject

class TransactionPageActivityModel @Inject constructor(
    private val transactionService: TransactionService
): ViewModel() {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: MutableLiveData<Page<Transaction>> = MutableLiveData(Page())

    fun loading() {
        nextTransactionPageLoading()
    }

    fun nextTransactionPageLoading(offset: Int = 0, numberOfRows: Int = 10) = transactionService
        .getAll(offset = offset, numberOfRows = numberOfRows)
        .let {
            pending.postValue(false)
            page.postValue(page.value.concat(it))
        }
}

