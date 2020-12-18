package com.github.onotoliy.opposite.treasure.di.resource

import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Call
import retrofit2.http.*

interface EventResource {

    @GET("/api/treasure/v1/event/sync")
    fun sync(
        @Header("Authorization") token: String,
        @Query("version") version: Long,
        @Query("offset") offset: Int,
        @Query("numberOfRows") numberOfRows: Int
    ): Call<Page<Event>>

    @POST("/api/treasure/v1/event")
    fun post(@Header("Authorization") token: String, @Body dto: Event): Call<Event>

    @PUT("/api/treasure/v1/event")
    fun put(@Header("Authorization") token: String, @Body dto: Event): Call<Event>

    @GET("/api/treasure/v1/event/version")
    fun version(@Header("Authorization") token: String): Call<Option>
}