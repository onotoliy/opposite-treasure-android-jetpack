package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.ui.components.LabeledText
import com.github.onotoliy.opposite.treasure.utils.fromISO
import com.github.onotoliy.opposite.treasure.utils.toShortDate

@Composable
fun EventView(data: EventVO, navigateTo: (Screen) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LabeledText(
            modifier = Modifier.fillMaxWidth(0.8f),
            label = stringResource(id = R.string.event_name),
            value = data.name
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(0.8f),
            label = stringResource(id = R.string.event_contribution),
            value = data.contribution
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(0.8f),
            label = stringResource(id = R.string.event_total),
            value = data.total
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(0.8f),
            label = stringResource(id = R.string.event_deadline),
            value = data.deadline.fromISO().toShortDate()
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(0.8f),
            label = stringResource(id = R.string.event_creation_date),
            value = data.creationDate.fromISO().toShortDate()
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(0.8f),
            label = stringResource(id = R.string.event_author),
            value = data.author.name,
            onClick = { navigateTo(Screen.DepositScreen(data.author.uuid)) }
        )
    }
}
