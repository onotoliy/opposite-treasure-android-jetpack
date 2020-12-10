package com.github.onotoliy.opposite.treasure.di.resource

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class ResourceModule {

    @Provides
    fun provideTransactionResource(retrofit: Retrofit): TransactionResource =
        retrofit.create(TransactionResource::class.java)

    @Provides
    fun provideCashboxResource(retrofit: Retrofit): CashboxResource =
        retrofit.create(CashboxResource::class.java)

    @Provides
    fun provideDepositResource(retrofit: Retrofit): DepositResource =
        retrofit.create(DepositResource::class.java)

    @Provides
    fun provideEventResource(retrofit: Retrofit): EventResource =
        retrofit.create(EventResource::class.java)

    @Provides
    fun provideDebtResource(retrofit: Retrofit): DebtResource =
        retrofit.create(DebtResource::class.java)

    @Provides
    fun provideRetrofit(): Retrofit = Retrofit
        .Builder()
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().setLenient().create())
        )
        .baseUrl("http://185.12.95.242/")
        .client(
            OkHttpClient
                .Builder()
                .writeTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build()
        )
        .build()
}