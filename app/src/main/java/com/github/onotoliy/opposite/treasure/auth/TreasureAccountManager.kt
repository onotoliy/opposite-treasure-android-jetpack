package com.github.onotoliy.opposite.treasure.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import android.util.Base64
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.nio.charset.StandardCharsets

fun AccountManager.addAccount(username: String, password: String, token: String) =
    addAccountExplicitly(Account(username, ACCOUNT_TYPE), password, token.userdata())

fun AccountManager.getAccount(): Account =
    getAccountsByType(ACCOUNT_TYPE)[0] as Account

fun AccountManager.getUUID(): String =
    getUserData(getAccount(), "uuid")

fun AccountManager.getAuthToken(): String =
    getAccount().run {
        authToken(name, getPassword(this))
    }

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
