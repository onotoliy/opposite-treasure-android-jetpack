package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.formatDate
import com.github.onotoliy.opposite.treasure.ui.BODY
import com.github.onotoliy.opposite.treasure.ui.H6_BOLD

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
        Text(text = data.deadline.formatDate(), style = BODY)

        Text(text = stringResource(id = R.string.event_creation_date), style = H6_BOLD)
        Text(text = data.creationDate.formatDate(), style = BODY)

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