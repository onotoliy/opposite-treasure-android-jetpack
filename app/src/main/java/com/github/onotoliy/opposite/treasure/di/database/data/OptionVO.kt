package com.github.onotoliy.opposite.treasure.di.database.data

import androidx.room.ColumnInfo
import com.github.onotoliy.opposite.data.Option

data class OptionVO(
    @ColumnInfo(name = "uuid", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var uuid: String = "",
    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT, defaultValue = "")
    var name: String = "",
)

fun Option.toVO(): OptionVO = OptionVO(uuid, name)
