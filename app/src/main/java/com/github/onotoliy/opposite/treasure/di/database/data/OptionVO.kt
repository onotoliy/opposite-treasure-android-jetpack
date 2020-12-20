package com.github.onotoliy.opposite.treasure.di.database.data

import androidx.room.ColumnInfo
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.data.core.HasName
import com.github.onotoliy.opposite.data.core.HasUUID

data class OptionVO(
    @ColumnInfo(name = "uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    override var uuid: String = "",
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    override var name: String = "",
): HasUUID, HasName

fun Option.toVO(): OptionVO = OptionVO(uuid, name)
fun OptionVO.toDTO(): Option = Option(uuid, name)

fun TransactionType.fromTransactionType() = OptionVO(name, label)
fun OptionVO.toTransactionType(): TransactionType = when(name) {
    "NONE"-> TransactionType.NONE
    "COST"-> TransactionType.COST
    "CONTRIBUTION"-> TransactionType.CONTRIBUTION
    "WRITE_OFF"-> TransactionType.WRITE_OFF
    "PAID"-> TransactionType.PAID
    "EARNED"-> TransactionType.EARNED
    else -> TransactionType.NONE
}
