package com.github.onotoliy.opposite.treasure.activity.model

import android.accounts.AccountManager
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.PageViewModel
import com.github.onotoliy.opposite.treasure.activity.DepositPageCallback
import com.github.onotoliy.opposite.treasure.numberOfRows
import com.github.onotoliy.opposite.treasure.offset
import com.github.onotoliy.opposite.treasure.services.deposits

class DepositPageActivityModel(
    private val manager: AccountManager
) {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: MutableLiveData<PageViewModel<Deposit>> = MutableLiveData(PageViewModel())

    fun loading() {
        nextDepositPageLoading()
    }

    fun nextDepositPageLoading(offset: Int = 0, numberOfRows: Int = 10) = manager
        .deposits
        .getAll(offset, numberOfRows)
        .enqueue(DepositPageCallback(page.value?.context ?: Page()) {
            page.postValue(
                PageViewModel(
                    offset = it.offset,
                    numberOfRows = it.numberOfRows,
                    context = it
                )
            )
            pending.postValue(false)
        })
}