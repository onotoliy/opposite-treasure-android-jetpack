package com.github.onotoliy.opposite.treasure.utils

import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
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

val defaultTransaction = TransactionVO (
    transactionDate = Date().toISO(),
    creationDate = Date().toISO()
)