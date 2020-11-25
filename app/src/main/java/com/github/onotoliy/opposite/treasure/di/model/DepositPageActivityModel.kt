package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.concat
import com.github.onotoliy.opposite.treasure.di.service.DepositService

class DepositPageActivityModel(
    private val depositService: DepositService
) {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: MutableLiveData<Page<Deposit>> = MutableLiveData(Page())

    fun loading() {
        nextDepositPageLoading()
    }

    fun nextDepositPageLoading(offset: Int = 0, numberOfRows: Int = 10) =
        depositService.getAll(offset, numberOfRows).let {
            pending.postValue(false)
            page.postValue(page.value.concat(it))
        }
}