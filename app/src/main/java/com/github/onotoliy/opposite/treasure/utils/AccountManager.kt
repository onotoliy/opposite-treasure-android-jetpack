package com.github.onotoliy.opposite.treasure.utils

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*

const val ACCOUNT_TYPE = "com.github.onotoliy.opposite.treasure"

fun AccountManager.getNewAuthToken(): String {
    val account = getAccount()
    val exp: String? = getUserData(account, "exp")
    val token: String? = getUserData(account, "token")

    if (exp.isNullOrEmpty() || token.isNullOrEmpty()) {
        Log.i("AccountManager", "exp or token is null $exp/$token")

        return getAuthToken()?.let {
            val bundle = it.userdata()

            setUserData(account, "exp", bundle.getString("exp"))
            setUserData(account, "token", it)

            it
        } ?: throw IllegalArgumentException()
    } else {
        val millis: Long = exp.toLong() * 1_000 + 180_000

        Log.i("AccountManager", "Millis $millis. Current ${System.currentTimeMillis()}, Exp $exp")

        return if (millis > System.currentTimeMillis()) {
            Log.i("AccountManager", "Hashed token")

            token
        } else {
            getAuthToken()?.let {
                Log.i("AccountManager", "Get new token")

                val bundle = it.userdata()

                setUserData(account, "exp", bundle.getString("exp"))
                setUserData(account, "token", it)

                it
            } ?: throw IllegalArgumentException()
        }
    }
}

fun AccountManager.addAccount(username: String, password: String, token: String) =
    addAccountExplicitly(Account(username, ACCOUNT_TYPE), password, token.userdata())

fun AccountManager.getAccount(): Account =
    getAccountsByType(ACCOUNT_TYPE)[0] as Account

fun AccountManager.getUUID(): String =
    getUserData(getAccount(), "uuid")

fun AccountManager.getName(): String =
    getUserData(getAccount(), "preferredName")

private fun String.userdata(): Bundle {
    val parts = split(".")
    if (parts.size != 3) {
        throw IllegalArgumentException("The token should consist of three parts.")
    }

    val payload = String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.ISO_8859_1)
    val node = JSONObject(payload)

    return Bundle().apply {
        putString("token", this@userdata)
        putString("exp", node.getLong("exp").toString())
        putString("uuid", node.getString("sub"))
        putString("lastName", node.getString("family_name"))
        putString("firstName", node.getString("given_name"))
        putString("preferredName", node.getString("preferred_username"))
    }
}
