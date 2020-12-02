package com.github.onotoliy.opposite.treasure.di.model

import android.accounts.AccountManager
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.di.service.DebtService
import com.github.onotoliy.opposite.treasure.di.service.DepositService
import com.github.onotoliy.opposite.treasure.di.service.EventService
import com.github.onotoliy.opposite.treasure.di.service.TransactionService
import com.github.onotoliy.opposite.treasure.utils.*
import java.util.*
import javax.inject.Inject

class TransactionEditActivityModel @Inject constructor(
    private val transactionService: TransactionService,
    private val depositService: DepositService,
    private val eventService: EventService,
    private val debtService: DebtService,
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
            Transaction

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
            val transaction = transactionService.get(pk)

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

    fun merge() {
        transactionService.merge(
            Transaction(
                uuid = uuid.value ?: "",
                name = name.value ?: "",
                cash = cash.value ?: "",
                person = person.value,
                event = event.value,
                type = TransactionType.valueOf(type.value?.uuid ?: ""),
                creationDate = creationDate.value ?: "",
                author = author.value ?: Option()
            )
        )
    }

    private fun postValueEvents(person: String?, type: String?) {
        if (!person.isNullOrEmpty() && !type.isNullOrEmpty()) {
            val tt = TransactionType.valueOf(type)

            if (tt == TransactionType.CONTRIBUTION || tt == TransactionType.WRITE_OFF) {
                events.postValue(
                    debtService
                        .getDebtAll(person, 0, 20)
                        .context
                        .map { Option(it.uuid, it.name) }
                )

                return
            }
        }

        events.postValue(eventService.getAllOption(this.event.value?.name))
    }

    fun getPersons(name: String? = null): List<Option> {
        return if (name.isNullOrEmpty()) {
            depositService.getAllOption(null)
        } else {
            depositService.getAllOption(name)
        }
    }

    fun getEvents(name: String? = null): List<Option> {
        return if (name.isNullOrEmpty()) {
            eventService.getAllOption(null)
        } else {
            eventService.getAllOption(name)
        }
    }
}