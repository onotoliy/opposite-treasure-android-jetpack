package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.concat
import com.github.onotoliy.opposite.treasure.di.service.EventService
import javax.inject.Inject

class EventPageActivityModel @Inject constructor(
    private val eventService: EventService
) {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: MutableLiveData<Page<Event>> = MutableLiveData(Page())

    fun loading() {
        nextEventPageLoading()
    }

    fun nextEventPageLoading(offset: Int = 0, numberOfRows: Int = 10) =
        eventService.getAll(offset, numberOfRows).let {
            pending.postValue(false)
            page.postValue(page.value.concat(it))
        }
}

