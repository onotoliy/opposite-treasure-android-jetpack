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

class CashboxCallback(onResponse: (Cashbox?) -> Unit) : DefaultCallback<Cashbox>(onResponse)