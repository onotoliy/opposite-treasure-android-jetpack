package com.github.onotoliy.opposite.treasure.di

import com.github.onotoliy.opposite.treasure.di.database.*
import com.github.onotoliy.opposite.treasure.di.resource.*
import com.github.onotoliy.opposite.treasure.di.service.*
import dagger.Module
import dagger.Provides

@Module
class ServiceModel {

    @Provides
    fun provideDebtService(
        dao: DebtDAO, retrofit: DebtResource
    ): DebtService = DebtService(dao, retrofit)

    @Provides
    fun provideEventService(
        dao: EventDAO, retrofit: EventResource
    ): EventService = EventService(dao, retrofit)

    @Provides
    fun provideDepositService(
        dao: DepositDAO, retrofit: DepositResource
    ): DepositService = DepositService(dao, retrofit)

    @Provides
    fun provideCashboxService(
        dao: CashboxDAO, retrofit: CashboxResource
    ): CashboxService = CashboxService(dao, retrofit)

    @Provides
    fun provideTransactionService(
        dao: TransactionDAO, retrofit: TransactionResource
    ): TransactionService = TransactionService(dao, retrofit)
}