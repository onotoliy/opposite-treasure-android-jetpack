package com.github.onotoliy.opposite.treasure.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import android.util.Base64
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.onotoliy.opposite.treasure.resources.CashboxResource
import com.github.onotoliy.opposite.treasure.resources.DepositResource
import com.github.onotoliy.opposite.treasure.resources.EventResource
import com.github.onotoliy.opposite.treasure.resources.TransactionResource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets

fun AccountManager.addAccount(username: String, password: String, token: String) =
    addAccountExplicitly(Account(username, ACCOUNT_TYPE), password, token.userdata())

fun AccountManager.getAccount(): Account =
    getAccountsByType(ACCOUNT_TYPE)[0] as Account

fun AccountManager.getUUID(): String =
    getUserData(getAccount(), "uuid")


val AccountManager.retrofit: Retrofit
    get() = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("http://185.12.95.242/")
        .client(client)
        .build()

val AccountManager.events: EventResource
    get() = retrofit.create(EventResource::class.java)

val AccountManager.transactions: TransactionResource
    get() = retrofit.create(TransactionResource::class.java)

val AccountManager.deposits: DepositResource
    get() = retrofit.create(DepositResource::class.java)

val AccountManager.cashbox: CashboxResource
    get() = retrofit.create(CashboxResource::class.java)

private val gson: Gson = GsonBuilder().setLenient().create()

private val AccountManager.client: OkHttpClient
    get() = OkHttpClient
        .Builder()
        .addInterceptor {
            val account = getAccount()
            val password = getPassword(account)
            val token = asyncAuthToken(account.name, password)
            val request: Request =
                it.request().newBuilder().addHeader("Authorization", "Bearer $token").build()

            it.proceed(request)
        }
        .build()

private fun String.userdata(): Bundle {
    val parts = split(".")
    if (parts.size != 3) {
        throw IllegalArgumentException("The token should consist of three parts.")
    }
    val payload = String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.ISO_8859_1)
    val node: JsonNode = ObjectMapper()
        .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)
        .readTree(payload)

    return Bundle().apply {
        putString("uuid", node["sub"].asText())
        putString("lastName", node["family_name"].asText())
        putString("firstName", node["given_name"].asText())
        putString("preferredName", node["preferred_username"].asText())
    }
}
