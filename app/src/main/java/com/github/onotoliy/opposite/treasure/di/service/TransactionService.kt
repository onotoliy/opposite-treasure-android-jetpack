package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.TransactionVO
import com.github.onotoliy.opposite.treasure.di.resource.TransactionResource
import javax.inject.Inject

class TransactionService @Inject constructor(
    private val dao: TransactionDAO,
    private val retrofit: TransactionResource
): AbstractService<Transaction>()  {
    fun get(pk: String): Transaction = dao.get(pk).toDTO() ?: Transaction()

    fun getAll(
        event: String? = null, person: String? = null, offset: Int = 0, numberOfRows: Int = 20
    ): Page<Transaction> {
        return Page()
    }

    override fun replace(dto: Transaction) = dao.replace(dto.toVO())

    override fun sync() = syncPage { offset, numberOfRows ->
        retrofit.sync(version = 0, offset = offset, numberOfRows = numberOfRows).execute()
    }
}

fun TransactionVO.toDTO(): Transaction {
    val event = eventUUID?.let { Option(it, eventName ?: "") }
    val person = personUUID?.let { Option(it, personName ?: "") }

    return Transaction(
        uuid = uuid,
        name = name,
        creationDate = creationDate,
        deletionDate = deletionDate,
        author = Option(authorUUID, authorName),
        event = event,
        person = person,
        cash = cash,
        type = TransactionType.valueOf(type),
        transactionDate = transactionDate
    )
}

fun Transaction.toVO() = TransactionVO(
    uuid = uuid,
    name = name,
    creationDate = creationDate,
    deletionDate = deletionDate,
    authorUUID = author.uuid,
    authorName = author.name,
    eventUUID = event?.uuid,
    eventName = event?.name,
    personUUID = person?.uuid,
    personName = person?.name,
    type = type.name,
    cash = cash
)