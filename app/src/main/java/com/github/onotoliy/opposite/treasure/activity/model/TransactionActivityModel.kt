package com.github.onotoliy.opposite.treasure.activity.model

import android.accounts.AccountManager
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.treasure.resources.TransactionCallback
import com.github.onotoliy.opposite.treasure.services.transactions

class TransactionActivityModel(
    private val pk: String,
    private val manager: AccountManager
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val transaction: MutableLiveData<Transaction> = MutableLiveData()

    fun loading() {
        manager.transactions.get(pk).enqueue(TransactionCallback {
            transaction.postValue(it)
        })
    }
}