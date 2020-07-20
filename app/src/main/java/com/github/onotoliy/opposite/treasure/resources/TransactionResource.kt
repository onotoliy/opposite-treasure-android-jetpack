package com.github.onotoliy.opposite.treasure.resources

import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionResource {
    @GET("/api/treasure/v1/transaction/{transaction}")
    fun get(@Path("transaction") transaction: String): Call<Transaction>

    @GET("/api/treasure/v1/transaction")
    fun getAll(
        @Query("offset") offset: Int = 0,
        @Query("numberOfRows") numberOfRows: Int = 20
    ): Call<Page<Transaction>>
}