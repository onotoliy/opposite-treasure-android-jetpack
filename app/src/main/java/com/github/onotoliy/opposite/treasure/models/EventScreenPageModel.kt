package com.github.onotoliy.opposite.treasure.models

import androidx.compose.Composable
import androidx.lifecycle.MutableLiveData
import androidx.ui.foundation.ScrollerPosition
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.tasks.EventResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventScreenPageModel(
    private val er: EventResource,
    val offset: Int = 0,
    val numberOfRows: Int = 20,
    private val default: List<Event> = listOf()
) {
    private val _events: MutableLiveData<MutableList<Event>> =
        MutableLiveData(default.toMutableList())

    private val _meta: MutableLiveData<Meta> =
        MutableLiveData()

    @Composable
    val events: List<Event>?
        get() = observe(data = _events)

    @Composable
    val meta: Meta?
        get() = observe(data = _meta)

    @Composable
    val scrollerPosition: ScrollerPosition
        get() = if (default.size < 10) ScrollerPosition() else ScrollerPosition((100 * default.size).toFloat())

    fun enqueue(): EventScreenPageModel {
        er.getAll(offset, numberOfRows).enqueue(object : Callback<Page<Event>> {
            override fun onFailure(call: Call<Page<Event>>, t: Throwable) { }
            override fun onResponse(call: Call<Page<Event>>, response: Response<Page<Event>>) {
                val list = _events.value ?: mutableListOf()
                list.addAll(response.body()?.context ?: mutableListOf())

                println("---1234567890---")

                _events.postValue(list)
                _meta.postValue(response.body()?.meta)
            }
        })

        return this
    }
}