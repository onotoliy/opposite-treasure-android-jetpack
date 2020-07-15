package com.github.onotoliy.opposite.treasure.ui.screens

import android.accounts.AccountManager
import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.Column
import androidx.ui.material.MaterialTheme
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.auth.getTransaction
import com.github.onotoliy.opposite.treasure.formatDate
import com.github.onotoliy.opposite.treasure.ui.typography

@Preview
@Composable
fun TransactionScreenPreview() {
    TransactionScreen(
        data = state {
            Transaction(
                uuid = "",
                name = "Открытие мотосезона город Улан-Удэ 2020 год",
                cash = "1 000",
                type = TransactionType.CONTRIBUTION,
                person = Option("", "Анатолий Похресный"),
                event = Option("", "Открытие мотосезона город Улан-Удэ 2020 год"),
                creationDate = "20.12.2020",
                author = Option("", "Анатолий Похресный")
            )
        }
    ) {

    }
}

@Composable
fun TransactionScreen(manager: AccountManager, uuid: String, navigateTo: (Screen) -> Unit) {
    val deposit = state { manager.getTransaction(uuid) }

    TransactionScreen(data = deposit, navigateTo = navigateTo)
}

@Composable
private fun TransactionScreen(data: MutableState<Transaction>, navigateTo: (Screen) -> Unit) {
    Column {
        Text(
            text = stringResource(id = R.string.transaction_name),
            style = typography.h6
        )
        Text(text = data.value.name, style = typography.body1)
        Text(
            text = stringResource(id = R.string.transaction_type),
            style = typography.h6
        )
        Text(text = data.value.type.label, style = typography.body1)
        Text(
            text = stringResource(id = R.string.transaction_cash),
            style = typography.h6
        )
        Text(text = data.value.cash, style = typography.body1)

        data.value.event?.let {
            Text(
                text = stringResource(id = R.string.transaction_event),
                style = typography.h6
            )
            Text(
                modifier = Modifier.clickable(onClick = {
                    navigateTo(Screen.EventScreen(it.uuid))
                }),
                text = it.name,
                style = typography.body1
            )
        }
        data.value.person?.let {
            Text(
                text = stringResource(id = R.string.transaction_person),
                style = typography.h6
            )
            Text(
                modifier = Modifier.clickable(onClick = {
                    navigateTo(Screen.DepositScreen(it.uuid))
                }),
                text = it.name,
                style = typography.body1
            )
        }
        Text(
            text = stringResource(id = R.string.event_creation_date),
            style = typography.h6
        )
        Text(text = data.value.creationDate.formatDate(), style = typography.body1)
        Text(text = stringResource(id = R.string.event_author), style = typography.h6)
        Text(
            modifier = Modifier.clickable(onClick = {
                navigateTo(Screen.DepositScreen(data.value.author.uuid))
            }),
            text = data.value.author.name,
            style = typography.body1
        )
    }
}