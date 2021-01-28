package com.github.onotoliy.opposite.treasure.di.database.repositories

import androidx.lifecycle.LiveData
import com.github.onotoliy.opposite.treasure.di.database.dao.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import java.util.*
import javax.inject.Inject

class EventRepository @Inject constructor(
    version: VersionDAO,
    dao: EventDAO
) : AbstractRepository<EventVO, EventDAO>("event", version, dao, dao::clean) {

    fun getAll(name: String?): LiveData<List<EventVO>> {
        return if (name == null) {
            dao.getAll()
        } else {
            dao.getAll("%${name.toLowerCase(Locale.getDefault())}%")
        }
    }
}
