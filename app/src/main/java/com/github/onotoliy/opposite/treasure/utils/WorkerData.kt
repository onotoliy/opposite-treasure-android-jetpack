package com.github.onotoliy.opposite.treasure.utils

import androidx.work.Data.Builder
import androidx.work.ListenableWorker
import androidx.work.WorkInfo
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import java.util.*

const val WORKER = "worker"
const val OFFSET = "offset"
const val TOTAL = "total"

val WorkInfo.event: EventVO
    get() = outputData.run {
        EventVO(
            uuid = getString("e_uuid") ?: "",
            name = getString("e_name") ?: "",
            contribution = getString("e_contribution") ?: "0.0",
            deadline = getString("e_deadline") ?: Date().toISO(),
            creationDate = getString("e_creationDate") ?: Date().toISO(),
        )
    }

val WorkInfo.transaction: TransactionVO
    get() = outputData.run {
        TransactionVO(
            uuid = getString("t_uuid") ?: "",
            name = getString("t_name") ?: "",
            cash = getString("t_cash") ?: "0.0",
            type = TransactionType.valueOf(getString("t_type") ?: "NONE"),
            transactionDate = getString("t_transactionDate") ?: Date().toISO(),
            person = if (getString("t_person").isNullOrEmpty()) null else OptionVO(getString("person") ?: "", "")
        )
    }

val WorkInfo.worker: String
    get() = progress.getString(WORKER) ?: ""

val WorkInfo.complete: Boolean
    get() = state == WorkInfo.State.FAILED || state == WorkInfo.State.SUCCEEDED

val WorkInfo.indicator: Float
    get() =
        if (complete) {
            1f
        } else {
            val offset = this.progress.getInt(OFFSET, 0)
            val total = this.progress.getInt(TOTAL, 0)

            when {
                offset == 0 || total == 0 -> 0f
                offset > total -> 1f
                else -> offset.toFloat() / total.toFloat()
            }
        }

fun progress(worker: String, total: Int = 0, offset: Int = 0) = Builder()
    .putString(WORKER, worker)
    .putInt(TOTAL, total)
    .putInt(OFFSET, offset)
    .build()

fun Builder.setWorker(name: String): Builder =
    putString(WORKER, name)

fun Builder.failure() =
    ListenableWorker.Result.failure(build())

fun Builder.success() =
    ListenableWorker.Result.success(build())

fun Builder.setTransaction(dto: TransactionVO) = this
    .putString("t_uuid", dto.uuid)
    .putString("t_name", dto.name)
    .putString("t_cash", dto.cash)
    .putString("t_type", dto.type.name)
    .putString("t_transactionDate", dto.transactionDate)
    .putString("t_person", dto.person?.name)

fun Builder.setEvent(dto: EventVO) = this
    .putString("e_uuid", dto.uuid)
    .putString("e_name", dto.name)
    .putString("e_contribution", dto.contribution)
    .putString("e_deadline", dto.deadline)
    .putString("e_creationDate", dto.creationDate)
