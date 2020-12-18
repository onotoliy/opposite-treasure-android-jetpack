package com.github.onotoliy.opposite.treasure.di.database

import com.github.onotoliy.opposite.treasure.di.database.dao.*
import com.github.onotoliy.opposite.treasure.di.database.repositories.*
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule(private val database: TreasureDatabase) {

    @Provides
    fun providesCashboxRepository(): CashboxRepository =
        CashboxRepository(providesCashboxDAO())

    @Provides
    fun providesDebtRepository(): DebtRepository =
        DebtRepository(providesVersionDAO(), providesDebtDAO())

    @Provides
    fun providesDepositRepository(): DepositRepository =
        DepositRepository(providesVersionDAO(), providesDepositDAO())

    @Provides
    fun providesEventRepository(): EventRepository =
        EventRepository(providesVersionDAO(), providesEventDAO())

    @Provides
    fun providesTransactionRepository(): TransactionRepository =
        TransactionRepository(providesVersionDAO(), providesTransactionDAO())

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
