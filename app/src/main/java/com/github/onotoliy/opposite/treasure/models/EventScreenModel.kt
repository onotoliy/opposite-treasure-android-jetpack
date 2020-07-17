package com.github.onotoliy.opposite.treasure.models

import androidx.compose.Composable
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.treasure.tasks.EventResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventScreenModel(
    private val er: EventResource,
    private val uuid: String
)  {
    private val _event: MutableLiveData<Event> = MutableLiveData()

    @Composable
    val event: Event?
        get() = observe(data = _event)

    fun enqueue(): EventScreenModel {
        er.get(uuid).enqueue(object : Callback<Event> {
            override fun onFailure(call: Call<Event>, t: Throwable) { }

            override fun onResponse(call: Call<Event>, response: Response<Event>) =
                _event.postValue(response.body())
        })

        return this
    }
}