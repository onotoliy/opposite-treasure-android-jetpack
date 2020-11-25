package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.PageViewModel
import com.github.onotoliy.opposite.treasure.di.service.DepositService
import com.github.onotoliy.opposite.treasure.numberOfRows
import com.github.onotoliy.opposite.treasure.offset

class DepositPageActivityModel(
    private val depositService: DepositService
) {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: MutableLiveData<PageViewModel<Deposit>> = MutableLiveData(PageViewModel())

    fun loading() {
        nextDepositPageLoading()
    }

    fun nextDepositPageLoading(offset: Int = 0, numberOfRows: Int = 10) =
        depositService.getAll(offset, numberOfRows).let {
            val context = page.value?.context?.context?.toMutableList() ?: mutableListOf()

            context.addAll(it.context)

            pending.postValue(false)

            page.postValue(
                PageViewModel(
                    offset = it.offset,
                    numberOfRows = it.numberOfRows,
                    context = Page(meta = it.meta, context = context)
                )
            )
        }
}