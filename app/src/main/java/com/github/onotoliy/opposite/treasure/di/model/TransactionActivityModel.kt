package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import javax.inject.Inject

class TransactionActivityModel @Inject constructor(
    private val dao: TransactionRepository
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val transaction: MutableLiveData<TransactionVO> = MutableLiveData()

    fun loading(pk: String) {
        dao.get(pk).observeForever {
            transaction.postValue(it)
        }
    }
}
