package com.github.onotoliy.opposite.treasure.di

import android.app.Application
import android.content.Context
import com.github.onotoliy.opposite.treasure.activity.model.DepositService
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideContext(): Context = application.applicationContext

    @Provides
    fun provideDepositService(): DepositService = DepositService(application)
}
