package com.github.onotoliy.opposite.treasure.utils

import android.annotation.SuppressLint
import androidx.work.Data
import androidx.work.WorkInfo
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import java.util.*

const val GLOBAL = 0
const val INSERT = 1
const val UPDATE = 2
const val DELETE = 3

val defaultTransactions: List<TransactionVO> = listOf()

val defaultEvents: List<EventVO> = listOf()

val defaultDeposits: List<DepositVO> = listOf()

val defaultOptions: List<OptionVO> = listOf()

val defaultCashbox = CashboxVO(
    deposit = "0.0",
    lastUpdateDate = Date().toISO(),
)

val defaultDeposit = DepositVO(
    uuid = uuid(),
    name = "",
    deposit = "0.0"
)

val defaultEvent = EventVO(
    uuid = uuid(),
    total = "0.0",
    contribution = "0.0",
    name = "",
    creationDate = Date().toISO(),
    deadline = Date().toISO(),
    deletionDate = null,
    exceptions = "",
    milliseconds = milliseconds(),
    local = GLOBAL,
    author = OptionVO()
)

val defaultTransaction = TransactionVO (
    uuid = uuid(),
    name = "",
    cash = "0.0",
    transactionDate = Date().toISO(),
    creationDate = Date().toISO(),
    deletionDate = null,
    type = TransactionType.NONE,
    author = OptionVO(),
    event = null,
    person = null,
    exceptions = "",
    local = GLOBAL,
    milliseconds = milliseconds()
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

fun uuid(): String = UUID.randomUUID().toString()
