package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import com.github.onotoliy.opposite.treasure.di.database.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.DepositVO
import com.github.onotoliy.opposite.treasure.di.service.toDTO
import javax.inject.Inject

class DepositPageActivityModel @Inject constructor(
    private val deposit: DepositDAO
) {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: MutableLiveData<Page<Deposit>> = MutableLiveData(Page())

    fun loading() {
        nextDepositPageLoading()
    }

    fun nextDepositPageLoading(offset: Int = 0, numberOfRows: Int = 10){
        pending.postValue(true)

        this.deposit.count().observeForever {
            val meta = Meta(it.toInt(), Paging(offset, numberOfRows))

            page.postValue(Page(meta, page.value?.context ?: listOf()))
        }

        this.deposit.getAll(offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            page.postValue(Page(
                page.value?.meta ?: Meta(),
                mutableListOf<Deposit>().apply {
                    addAll(page.value?.context ?: listOf())
                    addAll(list.map(DepositVO::toDTO))
                }
            ))
        }
    }
}