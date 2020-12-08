package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.*
import com.github.onotoliy.opposite.treasure.di.database.DebtVO

fun DebtVO.toDTO() = this.run {
    Debt(
        deposit = Deposit(
            person = Option(depositUserUUID, depositUsername),
            deposit = depositDeposit
        ),
        event = Event(
            uuid = eventUUID,
            name = eventName,
            contribution = eventContribution,
            total = eventTotal,
            deadline = eventDeadline,
            creationDate = eventCreationDate,
            author = Option(eventAuthorUUID, eventAuthorName),
            deletionDate = eventDeletionDate
        )
    )
}

fun Debt.toVO() = DebtVO(
    depositUserUUID = deposit.uuid,
    depositUsername = deposit.name,
    depositDeposit = deposit.deposit,
    eventUUID = event.uuid,
    eventName = event.name,
    eventContribution = event.contribution,
    eventTotal = event.total,
    eventDeadline = event.deadline,
    eventCreationDate = event.creationDate,
    eventAuthorUUID = event.author.uuid,
    eventAuthorName = event.author.name,
    eventDeletionDate = event.deletionDate
)