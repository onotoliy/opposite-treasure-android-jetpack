package com.github.onotoliy.opposite.treasure.di.model

import android.accounts.AccountManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.di.database.data.*
import com.github.onotoliy.opposite.treasure.di.database.repositories.DebtRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.DepositRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.EventRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import com.github.onotoliy.opposite.treasure.utils.*
import java.util.*
import javax.inject.Inject

class TransactionEditActivityModel @Inject constructor(
    private val transactionDAO: TransactionRepository,
    private val depositDAO: DepositRepository,
    private val eventDAO: EventRepository,
    private val debtDAO: DebtRepository,
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

    val qPersons: MutableLiveData<String> = MutableLiveData()
    val persons: MutableLiveData<List<OptionVO>> = MutableLiveData(listOf())

    val qEvents: MutableLiveData<String> = MutableLiveData()
    val events: MutableLiveData<List<OptionVO>> = MutableLiveData(listOf())

    fun postValue() {
        qPersons.observeForever { q ->
            depositDAO.getAll(q).observeForever { list ->
                persons.postValue(list.map { OptionVO(it.uuid, it.name) })
            }
        }

        qEvents.observeForever { q ->
            postValue(person.value?.uuid, q, type.value?.toTransactionType())
        }

        person.observeForever {
            postValue(it.uuid, qEvents.value, type.value?.toTransactionType())
        }

        type.observeForever {
            postValue(person.value?.uuid, qEvents.value, it.toTransactionType())
        }
    }

    fun postValue(person: String?, event: String?, type: TransactionType?) {
        if (type in listOf(TransactionType.CONTRIBUTION, TransactionType.WRITE_OFF)){
            eventDAO.getAll(event).observeForever { list ->
                events.postValue(list.map { OptionVO(it.uuid, it.name) })
            }
        } else {
            if (person.isNullOrEmpty()) {
                eventDAO.getAll(event).observeForever { list ->
                    events.postValue(list.map { OptionVO(it.uuid, it.name) })
                }
            } else {
                debtDAO.getByPersonAll(person, event).observeForever { list ->
                    events.postValue(list.map { OptionVO(it.event.uuid, it.event.name) })
                }
            }
        }
    }

    fun loading(pk: String?) {
        postValue()

        if (pk.isNullOrEmpty()) {
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
}