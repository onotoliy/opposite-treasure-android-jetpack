package com.github.onotoliy.opposite.treasure.di.resource

import android.accounts.AccountManager
import com.github.onotoliy.opposite.data.*
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Response
import javax.inject.Inject

class CashboxRetrofit @Inject constructor(
    private val retrofit: CashboxResource,
    private val account: AccountManager
) {
    fun get(): Response<Cashbox> =
        retrofit.get("Bearer " + account.getAuthToken()).execute()
}

class DebtRetrofit @Inject constructor(
    private val retrofit: DebtResource,
    private val account: AccountManager
): Retrofit<Debt> {
    override fun getVersion(): String =
        retrofit.version("Bearer " + account.getAuthToken()).execute().body()?.name ?: "0"

    override fun getAll(version: Long, offset: Int, numberOfRows: Int): Response<Page<Debt>> =
        retrofit.sync("Bearer " + account.getAuthToken(), version, offset, numberOfRows).execute()
}

class DepositRetrofit @Inject constructor(
    private val retrofit: DepositResource,
    private val account: AccountManager
): Retrofit<Deposit> {
    override fun getVersion(): String =
        retrofit.version("Bearer " + account.getAuthToken()).execute().body()?.name ?: "0"

    override fun getAll(version: Long, offset: Int, numberOfRows: Int): Response<Page<Deposit>> =
        retrofit.sync("Bearer " + account.getAuthToken(), version, offset, numberOfRows).execute()
}

class EventRetrofit @Inject constructor(
    private val retrofit: EventResource,
    private val account: AccountManager
): Retrofit<Event> {
    override fun getVersion(): String =
        retrofit.version("Bearer " + account.getAuthToken()).execute().body()?.name ?: "0"

    override fun getAll(version: Long, offset: Int, numberOfRows: Int): Response<Page<Event>> =
        retrofit.sync("Bearer " + account.getAuthToken(), version, offset, numberOfRows).execute()

    override fun saveOrUpdate(dto: Event): Response<Event> =
        retrofit.put("Bearer " + account.getAuthToken(), dto).execute()
}

class TransactionRetrofit @Inject constructor(
    private val retrofit: TransactionResource,
    private val account: AccountManager
): Retrofit<Transaction> {
    override fun getVersion(): String =
        retrofit.version("Bearer " + account.getAuthToken()).execute().body()?.name ?: "0"

    override fun getAll(version: Long, offset: Int, numberOfRows: Int): Response<Page<Transaction>> =
        retrofit.sync("Bearer " + account.getAuthToken(), version, offset, numberOfRows).execute()

    override fun saveOrUpdate(dto: Transaction): Response<Transaction> =
        retrofit.put("Bearer " + account.getAuthToken(), dto).execute()
}

interface Retrofit<T> {
    fun getVersion(): String
    fun getAll(version: Long, offset: Int, numberOfRows: Int): Response<Page<T>>
    fun saveOrUpdate(dto: T): Response<T> {
        throw UnsupportedOperationException()
    }
}
