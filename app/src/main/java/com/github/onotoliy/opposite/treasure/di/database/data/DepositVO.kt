package com.github.onotoliy.opposite.treasure.di.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.core.HasName
import com.github.onotoliy.opposite.data.core.HasUUID

@Entity(tableName = "treasure_deposit")
data class DepositVO(
    @PrimaryKey
    @ColumnInfo(name = "user_uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "1")
    override var uuid: String = "1",
    @ColumnInfo(name = "user_name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    override var name: String = "",
    @ColumnInfo(name = "deposit", typeAffinity = ColumnInfo.TEXT, defaultValue = "0.0")
    var deposit: String = "0.0"
): HasUUID, HasName

fun Deposit.toVO(): DepositVO = DepositVO(uuid, name, deposit)
