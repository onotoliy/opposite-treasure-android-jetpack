package com.github.onotoliy.opposite.treasure.di.restful

import com.github.onotoliy.opposite.treasure.di.restful.retrofit.CashboxRetrofit
import com.github.onotoliy.opposite.treasure.di.restful.retrofit.DebtRetrofit
import com.github.onotoliy.opposite.treasure.di.restful.retrofit.DepositRetrofit
import com.github.onotoliy.opposite.treasure.di.restful.retrofit.EventRetrofit
import com.github.onotoliy.opposite.treasure.di.restful.retrofit.TransactionRetrofit
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class RESTFulModule {

    @Provides
    fun provideTransactionResource(retrofit: Retrofit): TransactionRetrofit =
        retrofit.create(TransactionRetrofit::class.java)

    @Provides
    fun provideCashboxResource(retrofit: Retrofit): CashboxRetrofit =
        retrofit.create(CashboxRetrofit::class.java)

    @Provides
    fun provideDepositResource(retrofit: Retrofit): DepositRetrofit =
        retrofit.create(DepositRetrofit::class.java)

    @Provides
    fun provideEventResource(retrofit: Retrofit): EventRetrofit =
        retrofit.create(EventRetrofit::class.java)

    @Provides
    fun provideDebtResource(retrofit: Retrofit): DebtRetrofit =
        retrofit.create(DebtRetrofit::class.java)

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
                .writeTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build()
        )
        .build()
}
