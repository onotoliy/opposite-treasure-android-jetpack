package com.github.onotoliy.opposite.treasure.resources

import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DebtResource {
    @GET("/api/treasure/v1/debt/person/{person}")
    fun getAll(@Path("person") person: String): Call<Page<Event>>
}