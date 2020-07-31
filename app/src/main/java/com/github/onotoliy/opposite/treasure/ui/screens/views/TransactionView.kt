package com.github.onotoliy.opposite.treasure.ui.screens.views

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.Column
import androidx.ui.res.stringResource
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.formatDate
import com.github.onotoliy.opposite.treasure.ui.typography

@Composable
fun TransactionView(data: Transaction, navigateTo: (Screen) -> Unit) {
    Column {
        Text(
            text = stringResource(id = R.string.transaction_name),
            style = typography.h6
        )
        Text(text = data.name, style = typography.body1)
        Text(
            text = stringResource(id = R.string.transaction_type),
            style = typography.h6
        )
        Text(text = data.type.label, style = typography.body1)
        Text(
            text = stringResource(id = R.string.transaction_cash),
            style = typography.h6
        )
        Text(text = data.cash, style = typography.body1)

        data.event?.let {
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
        data.person?.let {
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
        Text(text = data.creationDate.formatDate(), style = typography.body1)
        Text(text = stringResource(id = R.string.event_author), style = typography.h6)
        Text(
            modifier = Modifier.clickable(onClick = {
                navigateTo(Screen.DepositScreen(data.author.uuid))
            }),
            text = data.author.name,
            style = typography.body1
        )
    }
}