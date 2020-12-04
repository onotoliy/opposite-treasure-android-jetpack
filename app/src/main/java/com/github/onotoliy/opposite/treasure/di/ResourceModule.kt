package com.github.onotoliy.opposite.treasure.di

import android.accounts.AccountManager
import com.github.onotoliy.opposite.treasure.di.resource.*
import com.github.onotoliy.opposite.treasure.utils.getAccount
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
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
    fun provideRetrofit(manager: AccountManager): Retrofit = Retrofit
        .Builder()
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().setLenient().create())
        )
        .baseUrl("http://185.12.95.242/")
        .client(
            OkHttpClient
                .Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .addInterceptor {
                    val token = manager.getAuthToken()
                    val request: Request =
                        it.request().newBuilder().addHeader("Authorization", "Bearer $token").build()


                    it.proceed(request)
                }
                .build()
        )
        .build()
}