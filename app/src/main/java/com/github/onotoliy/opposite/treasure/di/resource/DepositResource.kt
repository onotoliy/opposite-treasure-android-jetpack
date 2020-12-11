package com.github.onotoliy.opposite.treasure.di.resource

import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface DepositResource {

    @GET("/api/treasure/v1/deposit/sync")
    fun sync(
        @Header("Authorization") token: String,
        @Query("offset") version: Int,
        @Query("offset") offset: Int,
        @Query("numberOfRows") numberOfRows: Int
    ): Call<Page<Deposit>>

    @GET("/api/treasure/v1/deposit/version")
    fun version(@Header("Authorization") token: String): Option
}