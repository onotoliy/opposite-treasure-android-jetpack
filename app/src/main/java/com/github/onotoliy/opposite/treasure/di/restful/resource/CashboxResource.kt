package com.github.onotoliy.opposite.treasure.di.restful.resource

import android.accounts.AccountManager
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.treasure.di.restful.retrofit.CashboxRetrofit
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Response
import javax.inject.Inject

class CashboxResource @Inject constructor(
    private val retrofit: CashboxRetrofit,
    private val account: AccountManager
) {
    fun get(): Response<Cashbox> =
        retrofit.get(account.getAuthToken()).execute()
}
