package com.github.onotoliy.opposite.treasure.di.restful.retrofit

import com.github.onotoliy.opposite.data.core.ExceptionDevice
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ExceptionDeviceRetrofit {

    @POST("/api/treasure/v1/user/register/exception")
    fun register(@Body dto: ExceptionDevice): Call<Void>

}