package com.github.onotoliy.opposite.treasure.models

import androidx.compose.Composable
import androidx.lifecycle.MutableLiveData
import androidx.ui.foundation.ScrollerPosition
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.tasks.TransactionResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionScreenPageModel(
    private val tr: TransactionResource,
    val offset: Int = 0,
    val numberOfRows: Int = 20,
    private val default: List<Transaction> = listOf()
) {
    private val _transactions: MutableLiveData<MutableList<Transaction>> =
        MutableLiveData(default.toMutableList())
    private val _meta: MutableLiveData<Meta> =
        MutableLiveData()

    @Composable
    val transactions: List<Transaction>?
        get() = observe(data = _transactions)

    @Composable
    val meta: Meta?
        get() = observe(data = _meta)

    @Composable
    val scrollerPosition: ScrollerPosition
        get() = if (default.size < 10) ScrollerPosition() else ScrollerPosition((100 * default.size).toFloat())

    fun enqueue(): TransactionScreenPageModel {
        tr.getAll(offset, numberOfRows).enqueue(object : Callback<Page<Transaction>> {
            override fun onFailure(call: Call<Page<Transaction>>, t: Throwable) { }
            override fun onResponse(call: Call<Page<Transaction>>, response: Response<Page<Transaction>>) {
                _transactions.value?.addAll(response.body()?.context ?: listOf())
            }
        })

        return this
    }
}