package com.github.onotoliy.opposite.treasure.resources

import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class DefaultCallback<T>(
    private val onResponse: (T?) -> Unit,
    private val onFailure: (Throwable) -> Unit = { }
) : Callback<T> {
    override fun onFailure(call: Call<T>, t: Throwable) = onFailure(t)
    override fun onResponse(call: Call<T>, response: Response<T>) = onResponse(response.body())
}

open class DefaultPageCallback<T>(
    private val default: Page<T>,
    private val onResponse: (Page<T>) -> Unit,
    private val onFailure: (Throwable) -> Unit = { }
) : Callback<Page<T>> {
    override fun onFailure(call: Call<Page<T>>, t: Throwable) = onFailure(t)
    override fun onResponse(call: Call<Page<T>>, response: Response<Page<T>>) {
        val page: Page<T> = response.body()?.run {
            val list = mutableListOf<T>()
            list.addAll(default.context)
            list.addAll(context)

            Page(meta = meta, context = list)
        } ?: default

        onResponse(page)
    }
}

class CashboxCallback(onResponse: (Cashbox?) -> Unit) : DefaultCallback<Cashbox>(onResponse)
class DepositCallback(onResponse: (Deposit?) -> Unit) : DefaultCallback<Deposit>(onResponse)
class EventCallback(onResponse: (Event?) -> Unit) : DefaultCallback<Event>(onResponse)
class TransactionCallback(onResponse: (Transaction?) -> Unit) :
    DefaultCallback<Transaction>(onResponse)

class DepositPageCallback(
    default: Page<Deposit>? = null,
    onResponse: (Page<Deposit>?) -> Unit
) : DefaultPageCallback<Deposit>(default = default ?: Page(), onResponse = onResponse)

class EventPageCallback(
    default: Page<Event>? = null,
    onResponse: (Page<Event>?) -> Unit
) : DefaultPageCallback<Event>(default = default ?: Page(), onResponse = onResponse)

class TransactionPageCallback(
    default: Page<Transaction>? = null,
    onResponse: (Page<Transaction>?) -> Unit
) : DefaultPageCallback<Transaction>(default = default ?: Page(), onResponse = onResponse)