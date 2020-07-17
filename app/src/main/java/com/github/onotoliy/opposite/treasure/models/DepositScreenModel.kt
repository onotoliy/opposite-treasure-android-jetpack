package com.github.onotoliy.opposite.treasure.models

import androidx.compose.Composable
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.treasure.tasks.CashboxResource
import com.github.onotoliy.opposite.treasure.tasks.DepositResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepositScreenModel(
    private val dr: DepositResource,
    private val cr: CashboxResource,
    private val person: String
) {

    private val _cashbox: MutableLiveData<Cashbox> = MutableLiveData(Cashbox("-", "-"))
    private val _deposit: MutableLiveData<Deposit> = MutableLiveData(Deposit(Option("-", "-"), "-"))

    @Composable
    val deposit: Deposit?
        get() = observe(data = _deposit)

    @Composable
    val cashbox: Cashbox?
        get() = observe(data = _cashbox)

    fun enqueue(): DepositScreenModel {
        cr.get().enqueue(object : Callback<Cashbox> {
            override fun onFailure(call: Call<Cashbox>, t: Throwable) { }

            override fun onResponse(call: Call<Cashbox>, response: Response<Cashbox>) =
                _cashbox.postValue(response.body())
        })
        dr.get(person).enqueue(object : Callback<Deposit> {
            override fun onFailure(call: Call<Deposit>, t: Throwable) { }
            override fun onResponse(call: Call<Deposit>, response: Response<Deposit>) =
                _deposit.postValue(response.body())
        })

        return this
    }
}

