package com.github.onotoliy.opposite.treasure.di.restful.resource

import android.accounts.AccountManager
import com.github.onotoliy.opposite.data.Debt
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.restful.retrofit.DebtRetrofit
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Response
import javax.inject.Inject

class DebtResource @Inject constructor(
    private val retrofit: DebtRetrofit,
    private val account: AccountManager
): Resource<Debt> {
    override fun getVersion(): String =
        retrofit.version(account.getAuthToken()).execute().body()?.name ?: "0"

    override fun getAll(version: Long, offset: Int, numberOfRows: Int): Response<Page<Debt>> =
        retrofit.sync(account.getAuthToken(), version, offset, numberOfRows).execute()
}
