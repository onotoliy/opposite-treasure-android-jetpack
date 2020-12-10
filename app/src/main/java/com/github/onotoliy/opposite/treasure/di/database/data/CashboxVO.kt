package com.github.onotoliy.opposite.treasure.di.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.onotoliy.opposite.data.Cashbox

@Entity(tableName = "treasure_cashbox")
data class CashboxVO(
    @PrimaryKey
    @ColumnInfo(name = "pk", typeAffinity = ColumnInfo.TEXT, defaultValue = "1")
    var pk: String = "1",
    @ColumnInfo(name = "deposit", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var deposit: String = "0.0",
    @ColumnInfo(name = "last_update_date", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var lastUpdateDate: String = ""
)

fun Cashbox.toVO() = CashboxVO(deposit, lastUpdateDate)