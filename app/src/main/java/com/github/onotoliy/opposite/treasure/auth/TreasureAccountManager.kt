package com.github.onotoliy.opposite.treasure.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import android.util.Base64
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.tasks.cashbox.CashboxTask
import com.github.onotoliy.opposite.treasure.tasks.deposit.DepositPageTask
import com.github.onotoliy.opposite.treasure.tasks.transaction.TransactionPageTask
import com.github.onotoliy.opposite.treasure.tasks.deposit.DepositTask
import com.github.onotoliy.opposite.treasure.tasks.event.EventPageTask
import com.github.onotoliy.opposite.treasure.tasks.event.EventTask
import com.github.onotoliy.opposite.treasure.tasks.transaction.TransactionTask
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

fun AccountManager.getCashbox(): Cashbox =
    CashboxTask(this).execute().get()

fun AccountManager.getDeposit(deposit: String): Deposit =
    DepositTask(this, deposit).execute().get()

fun AccountManager.getDepositPage(offset: Int = 0, numberOfRows: Int = 20): Page<Deposit> =
    DepositPageTask(this, offset, numberOfRows).execute().get()

fun AccountManager.getEvent(event: String): Event =
    EventTask(this, event).execute().get()

fun AccountManager.getEventPage(offset: Int = 0, numberOfRows: Int = 20): Page<Event> =
    EventPageTask(this, offset, numberOfRows).execute().get()

fun AccountManager.getTransaction(transaction: String): Transaction =
    TransactionTask(this, transaction).execute().get()

fun AccountManager.getTransactionPage(offset: Int = 0, numberOfRows: Int = 20): Page<Transaction> =
    TransactionPageTask(this, offset, numberOfRows).execute().get()

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
