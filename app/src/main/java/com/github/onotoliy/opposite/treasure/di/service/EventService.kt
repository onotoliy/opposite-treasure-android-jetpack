package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.EventRepository
import com.github.onotoliy.opposite.treasure.di.resource.EventResource
import javax.inject.Inject

class EventService @Inject constructor(
    private val repository: EventRepository,
    private val retrofit: EventResource
): AbstractService<Event>(repository) {

    fun get(pk: String): Event = repository.get(pk)

    fun getAll(offset: Int, limit: Int): Page<Event> = repository.getAll(offset, limit)

    override fun sync() = syncTransactional {
        val version = repository.version()

        syncPage { offset, numberOfRows ->
            retrofit.sync(version = version, offset = offset, numberOfRows = numberOfRows).execute()
        }
    }
}