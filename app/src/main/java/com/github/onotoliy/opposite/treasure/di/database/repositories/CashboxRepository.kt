package com.github.onotoliy.opposite.treasure.di.database.repositories

import androidx.lifecycle.LiveData
import com.github.onotoliy.opposite.treasure.di.database.dao.CashboxDAO
import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO
import javax.inject.Inject

class CashboxRepository @Inject constructor(private val dao: CashboxDAO) {
    fun get(): LiveData<CashboxVO> = dao.get()

    fun replace(deposit: String, lastUpdateDate: String) =
        dao.replace(CashboxVO(deposit = deposit, lastUpdateDate = lastUpdateDate))
}