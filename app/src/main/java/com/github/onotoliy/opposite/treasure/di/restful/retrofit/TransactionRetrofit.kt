package com.github.onotoliy.opposite.treasure.di.restful.retrofit

import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.SyncResponse
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface TransactionRetrofit {

    @GET("/api/treasure/v1/transaction/sync")
    fun sync(
        @Header("Authorization") token: String,
        @Query("version") version: Long,
        @Query("offset") offset: Int,
        @Query("numberOfRows") numberOfRows: Int
    ): Call<Page<Transaction>>

    @PUT("/api/treasure/v1/transaction/sync")
    fun put(@Header("Authorization") token: String, @Body dto: Transaction): Call<SyncResponse>

    @GET("/api/treasure/v1/transaction/version")
    fun version(@Header("Authorization") token: String): Call<Option>
}
