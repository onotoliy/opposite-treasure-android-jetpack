package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.ui.BODY
import com.github.onotoliy.opposite.treasure.ui.H6_BOLD
import com.github.onotoliy.opposite.treasure.utils.fromISO
import com.github.onotoliy.opposite.treasure.utils.toShortDate

@Composable
fun EventView(data: EventVO, navigateTo: (Screen) -> Unit) {
    Column {
        Text(text = stringResource(id = R.string.event_name), style = H6_BOLD)
        Text(text = data.name, style = BODY)

        Text(text = stringResource(id = R.string.event_contribution), style = H6_BOLD)
        Text(text = data.contribution, style = BODY)

        Text(text = stringResource(id = R.string.event_total), style = H6_BOLD)
        Text(text = data.total, style = BODY)

        Text(text = stringResource(id = R.string.event_deadline), style = H6_BOLD)
        Text(text = data.deadline.fromISO().toShortDate(), style = BODY)

        Text(text = stringResource(id = R.string.event_creation_date), style = H6_BOLD)
        Text(text = data.creationDate.fromISO().toShortDate(), style = BODY)

        Text(text = stringResource(id = R.string.event_author), style = H6_BOLD)
        Text(
            modifier = Modifier.clickable(onClick = {
                navigateTo(Screen.DepositScreen(data.author.uuid))
            }),
            text = data.author.name,
            style = BODY
        )
    }
}

@Composable
fun EventView(data: Event, navigateTo: (Screen) -> Unit) {
    Column {
        Text(text = stringResource(id = R.string.event_name), style = H6_BOLD)
        Text(text = data.name, style = BODY)

        Text(text = stringResource(id = R.string.event_contribution), style = H6_BOLD)
        Text(text = data.contribution, style = BODY)

        Text(text = stringResource(id = R.string.event_total), style = H6_BOLD)
        Text(text = data.total, style = BODY)

        Text(text = stringResource(id = R.string.event_deadline), style = H6_BOLD)
        Text(text = data.deadline.fromISO().toShortDate(), style = BODY)

        Text(text = stringResource(id = R.string.event_creation_date), style = H6_BOLD)
        Text(text = data.creationDate.fromISO().toShortDate(), style = BODY)

        Text(text = stringResource(id = R.string.event_author), style = H6_BOLD)
        Text(
            modifier = Modifier.clickable(onClick = {
                navigateTo(Screen.DepositScreen(data.author.uuid))
            }),
            text = data.author.name,
            style = BODY
        )
    }
}