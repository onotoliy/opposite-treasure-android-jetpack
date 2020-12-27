package com.github.onotoliy.opposite.treasure.di.restful.retrofit

import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface EventRetrofit {

    @GET("/api/treasure/v1/event/sync")
    fun sync(
        @Header("Authorization") token: String,
        @Query("version") version: Long,
        @Query("offset") offset: Int,
        @Query("numberOfRows") numberOfRows: Int
    ): Call<Page<Event>>

    @PUT("/api/treasure/v1/event/sync")
    fun put(@Header("Authorization") token: String, @Body dto: Event): Call<Event>

    @GET("/api/treasure/v1/event/version")
    fun version(@Header("Authorization") token: String): Call<Option>
}
