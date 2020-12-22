package com.github.onotoliy.opposite.treasure.di.database

import androidx.room.TypeConverter
import com.github.onotoliy.opposite.data.TransactionType

class Converters {
    @TypeConverter
    fun fromTransactionType(value: String?): TransactionType? =
        value?.let { TransactionType.valueOf(it) }

    @TypeConverter
    fun toTransactionType(value: TransactionType?): String? =
        value?.name
}
