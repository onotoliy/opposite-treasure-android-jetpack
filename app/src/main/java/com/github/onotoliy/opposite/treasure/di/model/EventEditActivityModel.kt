package com.github.onotoliy.opposite.treasure.di.model

import android.accounts.AccountManager
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.treasure.di.database.dao.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import com.github.onotoliy.opposite.treasure.utils.*
import java.util.*
import javax.inject.Inject

class EventEditActivityModel @Inject constructor(
    private val dao: EventDAO,
    private val manager: AccountManager
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)

    val uuid: MutableLiveData<String> = MutableLiveData("")
    val name: MutableLiveData<String> = MutableLiveData("")
    val contribution: MutableLiveData<String> = MutableLiveData("0.0")
    val total: MutableLiveData<String> = MutableLiveData("0.0")
    val deadline: MutableLiveData<String> = MutableLiveData("")
    private val creationDate: MutableLiveData<String> = MutableLiveData("")
    private val author: MutableLiveData<OptionVO> = MutableLiveData(OptionVO())

    fun loading(pk: String) {
        if (pk.isEmpty()) {
            uuid.postValue(randomUUID())
            name.postValue("")
            contribution.postValue("0.0")
            total.postValue("0.0")
            deadline.postValue("")
            creationDate.postValue(Date().toISO())
            author.postValue(OptionVO(uuid = manager.getUUID(), name = manager.getName()))
        } else {
            dao.get(pk).observeForever { it ->
                it.let { event ->
                    uuid.postValue(event.uuid)
                    name.postValue(event.name)
                    contribution.postValue(event.contribution)
                    total.postValue(event.total)
                    deadline.postValue(event.contribution)
                    creationDate.postValue(event.creationDate)
                    author.postValue(event.author)
                }
            }
        }
    }

    fun merge() {
        dao.replace(
            EventVO(
                uuid = uuid.value ?: "",
                name = name.value ?: "",
                contribution = contribution.value ?: "",
                total = total.value ?: "",
                deadline = deadline.value?.fromShortDate()?.toISO() ?: "",
                creationDate = creationDate.value ?: "",
                author = author.value ?: OptionVO()
            )
        )
    }
}