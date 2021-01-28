package com.github.onotoliy.opposite.treasure.di.database.repositories

import androidx.lifecycle.LiveData
import com.github.onotoliy.opposite.treasure.di.database.dao.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import java.util.*
import javax.inject.Inject

class DepositRepository @Inject constructor(
    version: VersionDAO,
    dao: DepositDAO
) : AbstractRepository<DepositVO, DepositDAO>("deposit", version, dao) {

    fun getAll(name: String?): LiveData<List<DepositVO>>  {
        return if (name == null) {
            dao.getAll()
        } else {
            dao.getAll("%${name.toLowerCase(Locale.getDefault())}%")
        }
    }
}
