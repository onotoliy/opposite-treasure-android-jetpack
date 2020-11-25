package com.github.onotoliy.opposite.treasure.activity.model

import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.database.*
import javax.inject.Inject

class DebtService @Inject constructor(private val helper: DebtHelper) {
    fun getAll(person: String, offset: Int, limit: Int): Page<Event> = Page()
}

class DebtorService @Inject constructor(private val helper: DebtorHelper) {
    fun getAll(event: String, offset: Int, limit: Int): Page<Deposit> = Page()
}

class TransactionService @Inject constructor(private val helper: TransactionHelper) {
    fun get(pk: String): Transaction = helper.get(pk)

    fun getAll(
        event: String? = null, person: String? = null, offset: Int = 0, numberOfRows: Int = 20
    ): Page<Transaction> = helper.getAll(event, person, offset, numberOfRows)
}

class EventService @Inject constructor(private val helper: EventHelper) {
    fun get(pk: String): Event = helper.get(pk)

    fun getAll(offset: Int, limit: Int): Page<Event> = helper.getAll(offset, limit)
}

class DepositService @Inject constructor(private val helper: DepositHelper) {
    fun get(pk: String): Deposit = helper.get(pk)

    fun getAll(offset: Int, limit: Int): Page<Deposit> = helper.getAll(offset, limit)
}

class CashboxService @Inject constructor(private val helper: CashboxHelper) {
    fun get(): Cashbox = helper.get()
}