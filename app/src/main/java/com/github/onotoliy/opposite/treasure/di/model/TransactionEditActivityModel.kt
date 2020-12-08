package com.github.onotoliy.opposite.treasure.di.model

import android.accounts.AccountManager
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.di.database.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.service.*
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
    val person: MutableLiveData<Option> = MutableLiveData(Option())
    val event: MutableLiveData<Option> = MutableLiveData(Option())
    val type: MutableLiveData<Option> = MutableLiveData(Option())
    private val author: MutableLiveData<Option> = MutableLiveData(Option())
    private val creationDate: MutableLiveData<String> = MutableLiveData("")

    val persons: MutableLiveData<List<Option>> = MutableLiveData(listOf())
    val events: MutableLiveData<List<Option>> = MutableLiveData(listOf())

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
            person.postValue(Option())
            event.postValue(Option())
            type.postValue(Option(TransactionType.NONE.name, TransactionType.NONE.label))
            author.postValue(Option(uuid = manager.getUUID(), name = manager.getName()))
            creationDate.postValue(Date().toISO())
        } else {
            transactionDAO.get(pk).observeForever {
                it.toDTO().let { transaction ->
                    uuid.postValue(transaction.uuid)
                    cash.postValue(transaction.cash)
                    name.postValue(transaction.name)
                    transactionDate.postValue(transaction.creationDate.fromISO().toShortDate())
                    person.postValue(transaction.person)
                    event.postValue(transaction.event)
                    type.postValue(transaction.type.run { Option(name, label) })
                    author.postValue(Option(uuid = manager.getUUID(), name = manager.getName()))
                }
            }
        }
    }

    fun merge() {
        transactionDAO.replace(
            Transaction(
                uuid = uuid.value ?: "",
                name = name.value ?: "",
                cash = cash.value ?: "",
                person = person.value,
                event = event.value,
                type = TransactionType.valueOf(type.value?.uuid ?: ""),
                creationDate = creationDate.value ?: "",
                author = author.value ?: Option()
            ).toVO()
        )
    }

    private fun postValueEvents(person: String?, type: String?) {

    }

    fun getPersons(name: String? = null): List<Option> {
        return emptyList()
    }

    fun getEvents(name: String? = null): List<Option> {
        return emptyList()
    }
}