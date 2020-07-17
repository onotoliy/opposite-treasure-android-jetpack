package com.github.onotoliy.opposite.treasure.models

import androidx.compose.Composable
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.treasure.tasks.TransactionResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionScreenModel(
    private val tr: TransactionResource,
    private val uuid: String
)  {
    private val _transaction: MutableLiveData<Transaction> = MutableLiveData()

    @Composable
    val transaction: Transaction?
        get() = observe(data = _transaction)

    fun enqueue(): TransactionScreenModel {
        tr.get(uuid).enqueue(object : Callback<Transaction> {
            override fun onFailure(call: Call<Transaction>, t: Throwable) { }

            override fun onResponse(call: Call<Transaction>, response: Response<Transaction>) =
                _transaction.postValue(response.body())
        })

        return this
    }
}