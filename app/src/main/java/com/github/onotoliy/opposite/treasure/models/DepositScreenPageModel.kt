package com.github.onotoliy.opposite.treasure.models

import android.graphics.pdf.PdfDocument
import androidx.compose.Composable
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.tasks.DepositResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepositScreenPageModel(
    private val dr: DepositResource,
    private val offset: Int = 0,
    private val numberOfRows: Int = 20,
    private val default: List<Deposit> = listOf()
) {
    private val _deposits: MutableLiveData<MutableList<Deposit>> =
        MutableLiveData(default.toMutableList())

    @Composable
    val deposits: List<Deposit>?
        get() = observe(data = _deposits)

    fun enqueue(): DepositScreenPageModel {
        dr.getAll(offset, numberOfRows).enqueue(object : Callback<Page<Deposit>> {
            override fun onFailure(call: Call<Page<Deposit>>, t: Throwable) { }
            override fun onResponse(call: Call<Page<Deposit>>, response: Response<Page<Deposit>>) {
                _deposits.value?.addAll(response.body()?.context ?: listOf())
            }
        })

        return this
    }
}