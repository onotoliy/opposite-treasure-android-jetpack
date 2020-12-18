package com.github.onotoliy.opposite.treasure.di.restful.resource

import android.accounts.AccountManager
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.restful.retrofit.DepositRetrofit
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Response
import javax.inject.Inject

class DepositResource @Inject constructor(
    private val retrofit: DepositRetrofit,
    private val account: AccountManager
): Resource<Deposit> {
    override fun getVersion(): String =
        retrofit.version("Bearer " + account.getAuthToken()).execute().body()?.name ?: "0"

    override fun getAll(version: Long, offset: Int, numberOfRows: Int): Response<Page<Deposit>> =
        retrofit.sync("Bearer " + account.getAuthToken(), version, offset, numberOfRows).execute()
}
