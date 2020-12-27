package com.github.onotoliy.opposite.treasure.utils

import android.annotation.SuppressLint
import androidx.work.Data
import androidx.work.WorkInfo
import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import java.util.*

fun randomUUID(): String = UUID.randomUUID().toString()

val defaultCashbox = CashboxVO(
    deposit = "0.0",
    lastUpdateDate = Date().toISO()
)

val defaultDeposit = DepositVO(
    name = "Неизвестный пользователь",
    deposit = "0.0"
)

val defaultTransactions: List<TransactionVO> = listOf()

val defaultEvents: List<EventVO> = listOf()

val defaultDeposits: List<DepositVO> = listOf()

val defaultOptions: List<OptionVO> = listOf()

val defaultEvent = EventVO(
    creationDate = Date().toISO(),
    deadline = Date().toISO()
)

val defaultTransaction = TransactionVO (
    transactionDate = Date().toISO(),
    creationDate = Date().toISO()
)

@SuppressLint("RestrictedApi")
val defaultWorkInfo = WorkInfo(
    UUID.randomUUID(),
    WorkInfo.State.ENQUEUED,
    Data.Builder().build(),
    listOf(),
    Data.Builder().build(),
    1
)