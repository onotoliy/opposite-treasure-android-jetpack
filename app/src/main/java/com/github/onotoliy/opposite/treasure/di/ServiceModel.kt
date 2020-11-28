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
        repository: DebtRepository, retrofit: DebtResource
    ): DebtService = DebtService(repository, retrofit)

    @Provides
    fun provideEventService(
        repository: EventRepository, retrofit: EventResource
    ): EventService = EventService(repository, retrofit)

    @Provides
    fun provideDepositService(
        repository: DepositRepository, retrofit: DepositResource
    ): DepositService = DepositService(repository, retrofit)

    @Provides
    fun provideCashboxService(
        repository: CashboxRepository, retrofit: CashboxResource
    ): CashboxService = CashboxService(repository, retrofit)

    @Provides
    fun provideTransactionService(
        repository: TransactionRepository, retrofit: TransactionResource
    ): TransactionService = TransactionService(repository, retrofit)
}