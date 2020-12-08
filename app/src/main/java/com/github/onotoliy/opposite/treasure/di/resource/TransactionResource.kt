package com.github.onotoliy.opposite.treasure.di.resource

import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionResource {

    @GET("/api/treasure/v1/transaction/sync")
    fun sync(
        @Header("Authorization") token: String,
        @Query("version") version: Int,
        @Query("offset") offset: Int,
        @Query("numberOfRows") numberOfRows: Int
    ): Call<Page<Transaction>>

    fun version(): Int
}