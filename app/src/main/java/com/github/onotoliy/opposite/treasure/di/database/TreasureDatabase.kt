package com.github.onotoliy.opposite.treasure.di.database

import androidx.room.*
import com.github.onotoliy.opposite.treasure.di.database.dao.*
import com.github.onotoliy.opposite.treasure.di.database.data.*

@Database(
    entities = [
        VersionVO::class,
        CashboxVO::class,
        DebtVO::class,
        DepositVO::class,
        EventVO::class,
        TransactionVO::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TreasureDatabase : RoomDatabase() {
    abstract fun versionDAO(): VersionDAO
    abstract fun cashboxDAO(): CashboxDAO
    abstract fun debtDAO(): DebtDAO
    abstract fun depositDAO(): DepositDAO
    abstract fun eventDAO(): EventDAO
    abstract fun transactionDAO(): TransactionDAO
}
