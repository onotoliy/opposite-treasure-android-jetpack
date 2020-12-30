package com.github.onotoliy.opposite.treasure.utils

import androidx.work.Data.Builder
import androidx.work.ListenableWorker
import androidx.work.WorkInfo
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import java.util.*

val WorkInfo.event: EventVO
    get() = this.outputData.run {
        EventVO(
            uuid = getString("e_uuid") ?: "",
            name = getString("e_name") ?: "",
            contribution = getString("e_contribution") ?: "0.0",
            deadline = getString("e_deadline") ?: Date().toISO(),
            creationDate = getString("e_creationDate") ?: Date().toISO(),
        )
    }

val WorkInfo.transaction: TransactionVO
    get() = this.outputData.run {
        TransactionVO(
            uuid = getString("t_uuid") ?: "",
            name = getString("t_name") ?: "",
            cash = getString("t_cash") ?: "0.0",
            type = TransactionType.valueOf(getString("t_type") ?: "NONE"),
            transactionDate = getString("t_transactionDate") ?: Date().toISO(),
            person = if (getString("t_person").isNullOrEmpty()) null else OptionVO(getString("person") ?: "", "")
        )
    }

val WorkInfo.total: Int
    get() = this.progress.getInt("total", 0)

val WorkInfo.worker: String
    get() = this.progress.getString("worker") ?: ""

val WorkInfo.offset: Int
    get() = this.progress.getInt("offset", 0)

val WorkInfo?.failed: Boolean
    get() = this?.state == WorkInfo.State.FAILED

val WorkInfo?.uuid: String
    get() = this?.outputData?.getString("uuid") ?: ""

val WorkInfo?.finished: Boolean
    get() = this?.outputData?.getBoolean("finished", false) ?: false

val WorkInfo.indicator: Float
    get() =
        if (finished) {
            1f
        } else {
            if (offset == 0 || total == 0) {
                0f
            } else {
                val indicator = offset.toFloat() / total.toFloat()
                if (indicator > 1.0f) 1.0f else indicator
            }
        }


fun progress(worker: String, total: Int = 0, offset: Int = 0) = Builder()
    .putString("worker", worker)
    .putInt("total", total)
    .putInt("offset", offset)
    .build()

fun Builder.setWorker(name: String): Builder =
    putString("worker", name)

fun Builder.setLocalVersion(version: String): Builder =
    putString("local-version", version)

fun Builder.setRemoteVersion(version: String): Builder =
    putString("remote-version", version)

fun Builder.setFinished(success: Boolean): Builder =
    putBoolean("finished", true).putBoolean("success", success)

fun Builder.failure() =
    ListenableWorker.Result.failure(setFinished(false).build())

fun Builder.success() =
    ListenableWorker.Result.success(setFinished(true).build())

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