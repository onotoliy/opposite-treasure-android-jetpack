package com.github.onotoliy.opposite.treasure.activity.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Transaction

class TransactionActivityModel(
    private val pk: String,
    private val transactionService: TransactionService
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val transaction: MutableLiveData<Transaction> = MutableLiveData()

    fun loading() {
        transaction.postValue(transactionService.get(pk))
        pending.postValue(false)
    }
}