package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.treasure.di.service.TransactionService
import javax.inject.Inject

class TransactionActivityModel @Inject constructor(
    private val transactionService: TransactionService
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val transaction: MutableLiveData<Transaction> = MutableLiveData()

    fun loading(pk: String) {
        transaction.postValue(transactionService.get(pk))
        pending.postValue(false)
    }
}