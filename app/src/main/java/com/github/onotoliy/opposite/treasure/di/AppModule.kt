package com.github.onotoliy.opposite.treasure.di

import android.app.Application
import android.content.Context
import com.github.onotoliy.opposite.treasure.di.database.*
import com.github.onotoliy.opposite.treasure.di.service.*
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideContext(): Context = application.applicationContext

    @Provides
    fun provideDebtService(): DebtService = DebtService(provideDebtHelper())

    @Provides
    fun provideDebtorService(): DebtorService = DebtorService(provideDebtorHelper())

    @Provides
    fun provideTransactionService(): TransactionService = TransactionService(provideTransactionHelper())

    @Provides
    fun provideEventService(): EventService = EventService(provideEventHelper())

    @Provides
    fun provideDepositService(): DepositService = DepositService(provideDepositHelper())

    @Provides
    fun provideCashboxService(): CashboxService = CashboxService(provideCashboxHelper())

    @Provides
    fun provideSQLiteHelper(): SQLiteDatabase = SQLiteDatabase(application.applicationContext)

    @Provides
    fun provideDebtHelper(): DebtHelper = DebtHelper(provideSQLiteHelper())

    @Provides
    fun provideDebtorHelper(): DebtorHelper = DebtorHelper(provideSQLiteHelper())

    @Provides
    fun provideTransactionHelper(): TransactionRepository = TransactionRepository(provideSQLiteHelper())

    @Provides
    fun provideEventHelper(): EventRepository = EventRepository(provideSQLiteHelper())

    @Provides
    fun provideDepositHelper(): DepositRepository = DepositRepository(provideSQLiteHelper())

    @Provides
    fun provideCashboxHelper(): CashboxRepository = CashboxRepository(provideSQLiteHelper())
}
