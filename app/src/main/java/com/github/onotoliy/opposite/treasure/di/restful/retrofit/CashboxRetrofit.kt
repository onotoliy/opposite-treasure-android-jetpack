package com.github.onotoliy.opposite.treasure.di.restful.retrofit

import com.github.onotoliy.opposite.data.Cashbox
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CashboxRetrofit {

    @GET("/api/treasure/v1/cashbox")
    fun get(@Header("Authorization") token: String): Call<Cashbox>

}