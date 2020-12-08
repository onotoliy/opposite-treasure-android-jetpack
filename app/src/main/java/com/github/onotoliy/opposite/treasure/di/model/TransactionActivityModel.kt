package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.treasure.di.database.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.service.toDTO
import javax.inject.Inject

class TransactionActivityModel @Inject constructor(
    private val dao: TransactionDAO
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val transaction: MutableLiveData<Transaction> = MutableLiveData()

    fun loading(pk: String) {
        dao.get(pk).observeForever {
            transaction.postValue(it.toDTO())
        }
    }
}