package com.github.onotoliy.opposite.treasure.di.restful.resource

import android.accounts.AccountManager
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.restful.retrofit.TransactionRetrofit
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Response
import javax.inject.Inject

class TransactionResource @Inject constructor(
    private val retrofit: TransactionRetrofit,
    private val account: AccountManager
): Resource<Transaction> {
    override fun getVersion(): String =
        retrofit.version("Bearer " + account.getAuthToken()).execute().body()?.name ?: "0"

    override fun getAll(version: Long, offset: Int, numberOfRows: Int): Response<Page<Transaction>> =
        retrofit.sync("Bearer " + account.getAuthToken(), version, offset, numberOfRows).execute()

    override fun saveOrUpdate(dto: Transaction): Response<Transaction> =
        retrofit.put("Bearer " + account.getAuthToken(), dto).execute()
}
