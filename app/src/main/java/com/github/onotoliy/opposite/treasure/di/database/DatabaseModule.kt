package com.github.onotoliy.opposite.treasure.di.database

import com.github.onotoliy.opposite.treasure.di.database.dao.*
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule(private val database: TreasureDatabase) {

    @Provides
    fun providesVersionDAO(): VersionDAO = database.versionDAO()

    @Provides
    fun providesCashboxDAO(): CashboxDAO = database.cashboxDAO()

    @Provides
    fun providesDebtDAO(): DebtDAO = database.debtDAO()

    @Provides
    fun providesDepositDAO(): DepositDAO = database.depositDAO()

    @Provides
    fun providesEventDAO(): EventDAO = database.eventDAO()

    @Provides
    fun providesTransactionDAO(): TransactionDAO = database.transactionDAO()
}
