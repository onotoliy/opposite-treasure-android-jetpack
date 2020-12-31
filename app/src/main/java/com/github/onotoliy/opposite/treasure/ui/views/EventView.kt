package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.ScrollableColumn
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
fun EventView(dto: EventVO, navigateTo: (Screen) -> Unit) {
    ScrollableColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.event_name),
            value = dto.name
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.event_contribution),
            value = dto.contribution
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.event_total),
            value = dto.total
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.event_deadline),
            value = dto.deadline.fromISO().toShortDate()
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.event_creation_date),
            value = dto.creationDate.fromISO().toShortDate()
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.event_author),
            value = dto.author.name,
            onClick = { navigateTo(Screen.DepositScreen(dto.author.uuid)) }
        )

        if (dto.exceptions.isNotBlank()) {
            LabeledText(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.event_exceptions),
                value = dto.exceptions
            )
        }
    }
}
