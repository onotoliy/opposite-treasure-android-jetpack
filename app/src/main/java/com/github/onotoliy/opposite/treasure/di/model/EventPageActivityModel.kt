package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.treasure.PageViewModel
import com.github.onotoliy.opposite.treasure.di.service.EventService
import com.github.onotoliy.opposite.treasure.numberOfRows
import com.github.onotoliy.opposite.treasure.offset

class EventPageActivityModel(
    private val eventService: EventService
) {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: MutableLiveData<PageViewModel<Event>> = MutableLiveData(PageViewModel())

    fun loading() {
        nextEventPageLoading()
    }

    fun nextEventPageLoading(offset: Int = 0, numberOfRows: Int = 10) =
        eventService.getAll(offset, numberOfRows).let {
            val context = page.value?.context?.context?.toMutableList() ?: mutableListOf()

            context.addAll(it.context)

            pending.postValue(false)

            page.postValue(
                PageViewModel(
                    offset = it.offset,
                    numberOfRows = it.numberOfRows,
                    context = it
                )
            )
        }
}

