package com.github.onotoliy.opposite.treasure.di.database.repositories

import androidx.lifecycle.LiveData
import com.github.onotoliy.opposite.treasure.di.database.dao.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.DebtVO
import java.util.*
import javax.inject.Inject

class DebtRepository @Inject constructor(
    version: VersionDAO,
    dao: DebtDAO
) : AbstractRepository<DebtVO, DebtDAO>("debt", version, dao) {

    fun countByEvent(person: String): LiveData<Long> =
        dao.countByEvent(person)

    fun getByEventAll(person: String, offset: Int, numberOfRows: Int): LiveData<List<DebtVO>> =
        dao.getByEventAll(person, offset, numberOfRows)

    fun countByPerson(person: String): LiveData<Long> =
        dao.countByPerson(person)

    fun getByPersonAll(person: String, offset: Int, numberOfRows: Int): LiveData<List<DebtVO>> =
        dao.getByPersonAll(person, offset, numberOfRows)

    fun getByPersonAll(person: String, name: String?): LiveData<List<DebtVO>> {
        return if (name.isNullOrEmpty()) {
            dao.getByPersonAll(person)
        } else {
            dao.getByPersonAll(person, "%${name.toLowerCase(Locale.getDefault())}%")
        }
    }
}
