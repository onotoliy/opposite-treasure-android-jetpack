package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.EventRepository
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage
import javax.inject.Inject

class EventPageActivityModel @Inject constructor(
    private val dao: EventRepository
) {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: LiveDataPage<EventVO> = LiveDataPage()

    fun loading() {
        dao.count().observeForever {
            page.total.postValue(it)
        }

        nextEventPageLoading()
    }

    fun nextEventPageLoading(offset: Int = 0, numberOfRows: Int = 10) {
        pending.postValue(true)

        dao.getAll(offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            page.offset = offset + numberOfRows
            page.numberOfRows = numberOfRows
            page.context.postValue(mutableListOf<EventVO>().apply{
                addAll(page.context.value ?: listOf())
                addAll(list)
            })
        }
    }
}

