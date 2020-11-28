package com.github.onotoliy.opposite.treasure.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.github.onotoliy.opposite.treasure.di.database.*
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class RepositoryModel {
    @Provides
    fun provideSQLiteDatabase(context: Context): SQLiteDatabase =
        SQLiteDatabase(context)

    @Provides
    fun provideDebtRepository(database: SQLiteDatabase): DebtRepository =
        DebtRepository(database)

    @Provides
    fun provideEventRepository(database: SQLiteDatabase): EventRepository =
        EventRepository(database)

    @Provides
    fun provideDepositRepository(database: SQLiteDatabase): DepositRepository =
        DepositRepository(database)

    @Provides
    fun provideCashboxRepository(database: SQLiteDatabase): CashboxRepository =
        CashboxRepository(database)

    @Provides
    fun provideTransactionRepository(database: SQLiteDatabase): TransactionRepository =
        TransactionRepository(database)
}
