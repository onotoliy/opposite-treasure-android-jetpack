package com.github.onotoliy.opposite.treasure.di.model

import android.accounts.AccountManager
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.EventRepository
import com.github.onotoliy.opposite.treasure.utils.*
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject

class EventEditActivityModel @Inject constructor(
    private val dao: EventRepository,
    private val manager: AccountManager
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)

    val uuid: MutableLiveData<String> = MutableLiveData("")
    val name: MutableLiveData<String> = MutableLiveData("")
    val contribution: MutableLiveData<String> = MutableLiveData("0.0")
    val total: MutableLiveData<String> = MutableLiveData("0.0")
    val deadline: MutableLiveData<String> = MutableLiveData("")
    private val updated: MutableLiveData<Int> = MutableLiveData(0)
    private val creationDate: MutableLiveData<String> = MutableLiveData("")
    private val author: MutableLiveData<OptionVO> = MutableLiveData(OptionVO())

    fun loading(pk: String?) {
        if (pk.isNullOrEmpty()) {
            uuid.postValue(randomUUID())
            name.postValue("")
            contribution.postValue("0.0")
            total.postValue("0.0")
            deadline.postValue("")
            updated.postValue(0)
            creationDate.postValue(Date().toISO())
            author.postValue(OptionVO(uuid = manager.getUUID(), name = manager.getName()))
        } else {
            dao.get(pk).observeForever {
                uuid.postValue(it.uuid)
                name.postValue(it.name)
                contribution.postValue(it.contribution)
                total.postValue(it.total)
                deadline.postValue(it.contribution)
                updated.postValue(1)
                creationDate.postValue(it.creationDate)
                author.postValue(it.author)
            }
        }
    }

    fun merge()  {
        Executors.newSingleThreadExecutor().execute {
            dao.replace(
                EventVO(
                    uuid = uuid.value ?: "",
                    name = name.value ?: "",
                    contribution = contribution.value ?: "",
                    total = total.value ?: "",
                    deadline = deadline.value?.fromShortDate()?.toISO() ?: "",
                    creationDate = creationDate.value ?: "",
                    author = author.value ?: OptionVO(),
                    local = 1,
                    updated = 0,
                    deletionDate = null
                )
            )
        }
    }
}