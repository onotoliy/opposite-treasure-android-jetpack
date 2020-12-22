package com.github.onotoliy.opposite.treasure.di.restful.resource

import android.accounts.AccountManager
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.restful.retrofit.EventRetrofit
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import retrofit2.Response
import javax.inject.Inject

class EventResource @Inject constructor(
    private val retrofit: EventRetrofit,
    private val account: AccountManager
): Resource<Event> {
    override fun getVersion(): String =
        retrofit.version( account.getAuthToken()).execute().body()?.name ?: "0"

    override fun getAll(version: Long, offset: Int, numberOfRows: Int): Response<Page<Event>> =
        retrofit.sync( account.getAuthToken(), version, offset, numberOfRows).execute()

    override fun saveOrUpdate(dto: Event): Response<Event> =
        retrofit.put( account.getAuthToken(), dto).execute()
}
