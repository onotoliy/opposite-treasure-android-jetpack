package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import com.github.onotoliy.opposite.treasure.di.database.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.EventVO
import com.github.onotoliy.opposite.treasure.di.service.toDTO
import javax.inject.Inject

class EventPageActivityModel @Inject constructor(
    private val dao: EventDAO
) {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: MutableLiveData<Page<Event>> = MutableLiveData(Page())

    fun loading() {
        nextEventPageLoading()
    }

    fun nextEventPageLoading(offset: Int = 0, numberOfRows: Int = 10) {
        pending.postValue(true)

        dao.count().observeForever {
            val meta = Meta(it.toInt(), Paging(offset, numberOfRows))

            page.postValue(Page(meta, page.value?.context ?: listOf()))
        }

        dao.getAll(offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            page.postValue(Page(
                page.value?.meta ?: Meta(),
                mutableListOf<Event>().apply {
                    addAll(page.value?.context ?: listOf())
                    addAll(list.map(EventVO::toDTO))
                }
            ))
        }
    }
}

