package com.github.onotoliy.opposite.treasure.di.resource

import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Call
import retrofit2.http.*

interface TransactionResource {

    @GET("/api/treasure/v1/transaction/sync")
    fun sync(
        @Header("Authorization") token: String,
        @Query("version") version: Int,
        @Query("offset") offset: Int,
        @Query("numberOfRows") numberOfRows: Int
    ): Call<Page<Transaction>>

    @POST("/api/treasure/v1/transaction")
    fun post(@Header("Authorization") token: String, @Body dto: Transaction): Call<Transaction>

    @PUT("/api/treasure/v1/transaction")
    fun put(@Header("Authorization") token: String, @Body dto: Transaction): Call<Transaction>

    @GET("/api/treasure/v1/transaction/version")
    fun version(@Header("Authorization") token: String): Option
}