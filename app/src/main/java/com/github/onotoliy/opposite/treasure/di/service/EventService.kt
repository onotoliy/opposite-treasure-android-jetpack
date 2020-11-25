package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.EventRepository
import javax.inject.Inject

class EventService @Inject constructor(private val helper: EventRepository) {
    fun get(pk: String): Event = helper.get(pk)

    fun getAll(offset: Int, limit: Int): Page<Event> = helper.getAll(offset, limit)
}