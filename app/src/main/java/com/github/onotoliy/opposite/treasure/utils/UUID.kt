package com.github.onotoliy.opposite.treasure.utils

import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import java.util.*

fun randomUUID(): String = UUID.randomUUID().toString()

val defaultCashbox = CashboxVO(
    deposit = "0.0",
    lastUpdateDate = Date().toShortDate()
)

val defaultDeposit = DepositVO(
    name = "Неизвестный пользователь",
    deposit = "0.0"
)

val defaultTransactions: List<TransactionVO> = listOf()

val defaultEvents: List<EventVO> = listOf()

val defaultDeposits: List<DepositVO> = listOf()

val defaultEvent = EventVO(
    creationDate = Date().toISO()
)

val defaultTransaction = TransactionVO (
    transactionDate = Date().toISO(),
    creationDate = Date().toISO()
)