package com.github.onotoliy.opposite.treasure.tasks

import com.github.onotoliy.opposite.data.Cashbox
import retrofit2.Call
import retrofit2.http.GET

interface CashboxResource {
    @GET("/api/treasure/v1/cashbox")
    fun get(): Call<Cashbox>
}