package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.LiveData
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import javax.inject.Inject

class TransactionActivityModel @Inject constructor(
    private val dao: TransactionRepository
) {

    lateinit var pk: String

    fun transaction(): LiveData<TransactionVO> = dao.get(pk)

    fun loading(pk: String) {
        this.pk = pk
    }
}
