package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.di.database.TransactionVO

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