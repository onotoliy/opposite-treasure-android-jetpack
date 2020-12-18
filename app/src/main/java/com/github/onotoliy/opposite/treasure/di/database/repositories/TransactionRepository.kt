package com.github.onotoliy.opposite.treasure.di.database.repositories

import androidx.lifecycle.LiveData
import com.github.onotoliy.opposite.treasure.di.database.dao.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    version: VersionDAO,
    dao: TransactionDAO
) : AbstractRepository<TransactionVO, TransactionDAO>("transaction", version, dao) {
    fun getByPersonAll(person: String, offset: Int, numberOfRows: Int): LiveData<List<TransactionVO>> =
        dao.getByPersonAll(person, offset, numberOfRows)

    fun countByPerson(person: String): LiveData<Long> =
        dao.countByPerson(person)

    fun getByEventAll(event: String, offset: Int, numberOfRows: Int): LiveData<List<TransactionVO>> =
        dao.getByEventAll(event, offset, numberOfRows)

    fun countByEvent(event: String): LiveData<Long> =
        dao.countByEvent(event)
}
