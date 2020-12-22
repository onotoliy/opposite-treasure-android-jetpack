package com.github.onotoliy.opposite.treasure.di.model

import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.DepositRepository
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage
import javax.inject.Inject

class DepositPageActivityModel @Inject constructor(
    private val dao: DepositRepository
) {

    val pending: MutableLiveData<Boolean> = MutableLiveData(true)
    val page: LiveDataPage<DepositVO> = LiveDataPage()

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
            page.context.postValue(mutableListOf<DepositVO>().apply{
                addAll(page.context.value ?: listOf())
                addAll(list)
            })
        }
    }
}
