package com.github.onotoliy.opposite.treasure.di.resource

import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventResource {

    @GET("/api/treasure/v1/event/sync")
    fun sync(
        @Query("version") version: Int = 0,
        @Query("offset") offset: Int = 0,
        @Query("numberOfRows") numberOfRows: Int = 20
    ): Call<Page<Event>>

}