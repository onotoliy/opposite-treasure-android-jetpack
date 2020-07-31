package com.github.onotoliy.opposite.treasure.resources

import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventResource {
    @GET("/api/treasure/v1/event/{event}")
    fun get(@Path("event") event: String): Call<Event>

    @GET("/api/treasure/v1/event/list")
    fun getAll(): Call<List<Option>>

    @GET("/api/treasure/v1/event")
    fun getAll(
        @Query("name") name: String = "",
        @Query("offset") offset: Int = 0,
        @Query("numberOfRows") numberOfRows: Int = 20
    ): Call<Page<Event>>
}