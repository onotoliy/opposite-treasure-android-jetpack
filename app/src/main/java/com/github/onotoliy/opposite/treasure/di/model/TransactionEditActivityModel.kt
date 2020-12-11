package com.github.onotoliy.opposite.treasure.di.model

import android.accounts.AccountManager
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.di.database.dao.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.utils.*
import java.util.*
import javax.inject.Inject

class TransactionEditActivityModel @Inject constructor(
    private val transactionDAO: TransactionDAO,
    private val depositDAO: DepositDAO,
    private val eventDAO: EventDAO,
    private val debtDAO: DebtDAO,
    private val manager: AccountManager
) {
    val pending: MutableLiveData<Boolean> = MutableLiveData(true)

    val uuid: MutableLiveData<String> = MutableLiveData("")
    val cash: MutableLiveData<String> = MutableLiveData("0.0")
    val name: MutableLiveData<String> = MutableLiveData("")
    val transactionDate: MutableLiveData<String> = MutableLiveData("")
    val person: MutableLiveData<OptionVO> = MutableLiveData(OptionVO())
    val event: MutableLiveData<OptionVO> = MutableLiveData(OptionVO())
    val type: MutableLiveData<OptionVO> = MutableLiveData(OptionVO())
    private val author: MutableLiveData<OptionVO> = MutableLiveData(OptionVO())
    private val creationDate: MutableLiveData<String> = MutableLiveData("")

    val persons: MutableLiveData<List<OptionVO>> = MutableLiveData(listOf())
    val events: MutableLiveData<List<OptionVO>> = MutableLiveData(listOf())

    init {
        person.observeForever { person ->
            postValueEvents(type.value?.uuid , person.uuid)
        }

        type.observeForever { type ->
            postValueEvents(type.uuid, person.value?.uuid)
        }
    }

    fun loading(pk: String) {
        if (pk.isEmpty()) {
            uuid.postValue(randomUUID())
            cash.postValue("0.0")
            name.postValue("")
            transactionDate.postValue("")
            person.postValue(OptionVO())
            event.postValue(OptionVO())
            type.postValue(OptionVO(TransactionType.NONE.name, TransactionType.NONE.label))
            author.postValue(OptionVO(uuid = manager.getUUID(), name = manager.getName()))
            creationDate.postValue(Date().toISO())
        } else {
            transactionDAO.get(pk).observeForever {
                it.let { transaction ->
                    uuid.postValue(transaction.uuid)
                    cash.postValue(transaction.cash)
                    name.postValue(transaction.name)
                    transactionDate.postValue(transaction.creationDate.fromISO().toShortDate())
                    person.postValue(transaction.person)
                    event.postValue(transaction.event)
                    type.postValue(transaction.type.run { OptionVO(name, label) })
                    author.postValue(OptionVO(uuid = manager.getUUID(), name = manager.getName()))
                }
            }
        }
    }

    fun merge() {
        transactionDAO.replace(
            TransactionVO(
                uuid = uuid.value ?: "",
                name = name.value ?: "",
                cash = cash.value ?: "",
                person = person.value,
                event = event.value,
                type = type.value?.let { TransactionType.valueOf(it.uuid) } ?: TransactionType.NONE,
                creationDate = creationDate.value ?: "",
                author = author.value ?: OptionVO()
            )
        )
    }

    private fun postValueEvents(person: String?, type: String?) {

    }

    fun getPersons(name: String? = null): List<OptionVO> {
        return emptyList()
    }

    fun getEvents(name: String? = null): List<OptionVO> {
        return emptyList()
    }
}