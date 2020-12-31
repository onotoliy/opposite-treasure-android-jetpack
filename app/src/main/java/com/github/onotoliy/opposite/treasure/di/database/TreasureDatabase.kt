package com.github.onotoliy.opposite.treasure.di.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.onotoliy.opposite.treasure.di.database.dao.CashboxDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO
import com.github.onotoliy.opposite.treasure.di.database.data.DebtVO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.database.data.VersionVO

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
