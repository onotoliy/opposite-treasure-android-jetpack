package com.github.onotoliy.opposite.treasure.di.restful.retrofit

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST

interface UserRetrofit {

    @POST("/api/treasure/v1/user/notification")
    fun notification(@Header("Authorization") token: String): Call<Void>
}
