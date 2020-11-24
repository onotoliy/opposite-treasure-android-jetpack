package com.github.onotoliy.opposite.treasure.activity

import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class DepositPageCallback(
    private val default: Page<Deposit> = Page(),
    private val onFailure: (Throwable) -> Unit = { },
    private val onResponse: (Page<Deposit>) -> Unit,
) : Callback<Page<Deposit>> {
    override fun onFailure(call: Call<Page<Deposit>>, t: Throwable) = onFailure(t)
    override fun onResponse(call: Call<Page<Deposit>>, response: Response<Page<Deposit>>) {
        val page: Page<Deposit> = response.body()?.run {
            val list = mutableListOf<Deposit>()
            list.addAll(default.context)
            list.addAll(context)

            Page(meta = meta, context = list)
        } ?: default

        onResponse(page)
    }
}

open class EventPageCallback(
    private val default: Page<Event> = Page(),
    private val onFailure: (Throwable) -> Unit = { },
    private val onResponse: (Page<Event>) -> Unit,
) : Callback<Page<Event>> {
    override fun onFailure(call: Call<Page<Event>>, t: Throwable) = onFailure(t)
    override fun onResponse(call: Call<Page<Event>>, response: Response<Page<Event>>) {
        val page: Page<Event> = response.body()?.run {
            val list = mutableListOf<Event>()
            list.addAll(default.context)
            list.addAll(context)

            Page(meta = meta, context = list)
        } ?: default

        onResponse(page)
    }
}

open class TransactionPageCallback(
    private val default: Page<Transaction> = Page(),
    private val onFailure: (Throwable) -> Unit = { },
    private val onResponse: (Page<Transaction>) -> Unit,
) : Callback<Page<Transaction>> {
    override fun onFailure(call: Call<Page<Transaction>>, t: Throwable) = onFailure(t)
    override fun onResponse(call: Call<Page<Transaction>>, response: Response<Page<Transaction>>) {
        val page: Page<Transaction> = response.body()?.run {
            val list = mutableListOf<Transaction>()
            list.addAll(default.context)
            list.addAll(context)

            Page(meta = meta, context = list)
        } ?: default

        onResponse(page)
    }
}
