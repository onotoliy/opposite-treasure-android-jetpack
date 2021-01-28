package com.github.onotoliy.opposite.treasure.utils

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import android.util.Base64
import org.json.JSONObject
import java.nio.charset.StandardCharsets

const val ACCOUNT_TYPE = "com.github.onotoliy.opposite.treasure"

fun AccountManager.addAccount(username: String, password: String, token: String) =
    addAccountExplicitly(Account(username, ACCOUNT_TYPE), password, token.userdata())

fun AccountManager.getAccount(): Account =
    getAccountsByType(ACCOUNT_TYPE)[0] as Account

fun AccountManager.getUUID(): String =
    getUserData(getAccount(), "uuid")

fun AccountManager.getName(): String =
    getUserData(getAccount(), "name")

fun AccountManager.isAdministrator(): Boolean =
    getUserData(getAccount(), "isAdministrator").toBoolean()

private fun String.userdata(): Bundle {
    val parts = split(".")
    if (parts.size != 3) {
        throw IllegalArgumentException("The token should consist of three parts.")
    }

    val payload = String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.ISO_8859_1)
    val node = JSONObject(payload)
    val roles = node.getJSONObject("realm_access").getJSONArray("roles")
    val isAdministrator = (0 until roles.length())
        .map(roles::getString)
        .any { it in listOf("president", "vice-president", "treasurer") }

    return Bundle().apply {
        putString("uuid", node.getString("sub"))
        putString("lastName", node.getString("family_name"))
        putString("firstName", node.getString("given_name"))
        putString("name", node.getString("name"))
        putString("isAdministrator", isAdministrator.toString())

    }
}
