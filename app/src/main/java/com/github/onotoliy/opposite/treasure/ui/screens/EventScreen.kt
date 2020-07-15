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
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.auth.getEvent
import com.github.onotoliy.opposite.treasure.formatDate
import com.github.onotoliy.opposite.treasure.ui.typography

@Preview
@Composable
fun EventScreenPreview() {
    EventScreen(
        data = state {
            Event(
                uuid = "",
                name = "Открытие мотосезона город Улан-Удэ 2020 год",
                contribution = "1 000",
                total = "10 000",
                deadline = "20.12.2020",
                creationDate = "20.12.2020",
                author = Option("", "Анатолий Похресный")
            )
        }
    ) {

    }
}

@Composable
fun EventScreen(manager: AccountManager, uuid: String, navigateTo: (Screen) -> Unit) {
    val deposit = state { manager.getEvent(uuid) }

    EventScreen(data = deposit, navigateTo = navigateTo)
}

@Composable
private fun EventScreen(data: MutableState<Event>, navigateTo: (Screen) -> Unit) {
    Column {
        Text(text = stringResource(id = R.string.event_name), style = typography.h6)
        Text(text = data.value.name, style = typography.body1)
        Text(text = stringResource(id = R.string.event_contribution), style = typography.h6)
        Text(text = data.value.contribution, style = typography.body1)
        Text(text = stringResource(id = R.string.event_total), style = typography.h6)
        Text(text = data.value.total, style = typography.body1)
        Text(text = stringResource(id = R.string.event_deadline), style = typography.h6)
        Text(text = data.value.deadline.formatDate(), style = typography.body1)
        Text(text = stringResource(id = R.string.event_creation_date), style = typography.h6)
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