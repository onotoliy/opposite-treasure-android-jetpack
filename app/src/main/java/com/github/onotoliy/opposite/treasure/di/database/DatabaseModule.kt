package com.github.onotoliy.opposite.treasure.di.database

import com.github.onotoliy.opposite.treasure.di.database.dao.CashboxDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.repositories.CashboxRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.DebtRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.DepositRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.EventRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
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
