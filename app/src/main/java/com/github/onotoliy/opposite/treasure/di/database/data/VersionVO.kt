package com.github.onotoliy.opposite.treasure.di.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "treasure_version")
data class VersionVO(
    @PrimaryKey
    @ColumnInfo(name = "type", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    val type: String = "",
    @ColumnInfo(name = "version", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    val version: String = "",
)
