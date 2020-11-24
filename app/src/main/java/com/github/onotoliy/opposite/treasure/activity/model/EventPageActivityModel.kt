package com.github.onotoliy.opposite.treasure.activity.model

import android.accounts.AccountManager
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.PageViewModel
import com.github.onotoliy.opposite.treasure.activity.EventPageCallback
import com.github.onotoliy.opposite.treasure.numberOfRows
import com.github.onotoliy.opposite.treasure.offset
import com.github.onotoliy.opposite.treasure.services.events

class EventPageActivityModel(
    private val manager: AccountManager
) {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: MutableLiveData<PageViewModel<Event>> = MutableLiveData(PageViewModel())

    fun loading() {
        nextEventPageLoading()
    }

    fun nextEventPageLoading(offset: Int = 0, numberOfRows: Int = 10) = manager
        .events
        .getAll(offset = offset, numberOfRows = numberOfRows)
        .enqueue(
            EventPageCallback(page.value?.context ?: Page()) {
                page.postValue(
                    PageViewModel(
                        offset = it.offset,
                        numberOfRows = it.numberOfRows,
                        context = it
                    )
                )
            }
        )
}

