package com.github.onotoliy.opposite.treasure.di

import android.accounts.AccountManager
import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideContext(): Context = application.applicationContext

    @Provides
    fun provideWorkManager() = WorkManager.getInstance(application)

    @Provides
    fun provideAccountManager(): AccountManager = AccountManager.get(application.applicationContext)
}
