package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import com.github.onotoliy.opposite.treasure.di.database.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.DepositVO
import com.github.onotoliy.opposite.treasure.di.service.toDTO
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage
import javax.inject.Inject

class DepositPageActivityModel @Inject constructor(
    private val dao: DepositDAO
) {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: LiveDataPage<Deposit> = LiveDataPage()

    fun loading() {
        dao.count().observeForever {
            page.total.postValue(it)
        }

        nextDepositPageLoading()
    }

    fun nextDepositPageLoading(offset: Int = 0, numberOfRows: Int = 10){
        pending.postValue(true)

        dao.getAll(offset, numberOfRows).observeForever { list ->
            pending.postValue(false)
            page.offset = offset + numberOfRows
            page.numberOfRows = numberOfRows
            page.context.postValue(mutableListOf<Deposit>().apply{
                addAll(page.context.value ?: listOf())
                addAll(list.map { it.toDTO() })
            })
        }
    }
}