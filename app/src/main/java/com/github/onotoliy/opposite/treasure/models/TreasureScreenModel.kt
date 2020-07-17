package com.github.onotoliy.opposite.treasure.models

import android.accounts.AccountManager
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.treasure.auth.asyncAuthToken
import com.github.onotoliy.opposite.treasure.auth.getAccount
import com.github.onotoliy.opposite.treasure.tasks.CashboxResource
import com.github.onotoliy.opposite.treasure.tasks.DepositResource
import com.github.onotoliy.opposite.treasure.tasks.EventResource
import com.github.onotoliy.opposite.treasure.tasks.TransactionResource
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TreasureScreenModel(private val manager: AccountManager) {
    private val client = OkHttpClient.Builder().addInterceptor {
        val account = manager.getAccount()
        val password = manager.getPassword(account)
        val token = asyncAuthToken(account.name, password)
        val request: Request = it.request().newBuilder().addHeader("Authorization", "Bearer $token").build()

        it.proceed(request)
    }
    private val gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit: Retrofit = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("http://185.12.95.242/")
        .client(client.build())
        .build()

    private val depositResource: DepositResource = retrofit.create(DepositResource::class.java)
    private val cashboxResource: CashboxResource = retrofit.create(CashboxResource::class.java)
    private val transactionResource: TransactionResource = retrofit.create(TransactionResource::class.java)
    private val eventResource: EventResource = retrofit.create(EventResource::class.java)

    fun getDepositModel(deposit: String) =
        DepositScreenModel(depositResource, cashboxResource, deposit).enqueue()
    fun getDepositPageModel(offset: Int = 0, numberOfRows: Int = 20, default: List<Deposit> = listOf()) =
        DepositScreenPageModel(depositResource, offset, numberOfRows, default).enqueue()

    fun getEventModel(event: String) =
        EventScreenModel(
            eventResource,
            event
        ).enqueue()
    fun getEventPageModel(offset: Int = 0, numberOfRows: Int = 20, default: List<Event> = listOf()) =
        EventScreenPageModel(
            eventResource,
            offset,
            numberOfRows,
            default
        ).enqueue()

    fun getTransactionModel(event: String) =
        TransactionScreenModel(
            transactionResource,
            event
        ).enqueue()
    fun getTransactionPageModel(offset: Int = 0, numberOfRows: Int = 20, default: List<Transaction> = listOf()) =
        TransactionScreenPageModel(
            transactionResource,
            offset,
            numberOfRows,
            default
        ).enqueue()
}