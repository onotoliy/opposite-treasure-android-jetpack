package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.ui.components.LabeledText
import com.github.onotoliy.opposite.treasure.utils.fromISO
import com.github.onotoliy.opposite.treasure.utils.toShortDate

@Composable
fun TransactionView(dto: TransactionVO, navigateTo: (Screen) -> Unit) {
    ScrollableColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.transaction_name),
            value = dto.name
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.transaction_type),
            value = dto.type.label
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.transaction_cash),
            value = dto.cash
        )

        dto.event?.let {
            LabeledText(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.transaction_event),
                value = it.name,
                onClick = { navigateTo(Screen.EventScreen(it.uuid)) }
            )
        }

        dto.person?.let {
            LabeledText(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.transaction_person),
                value = it.name,
                onClick = { navigateTo(Screen.DepositScreen(it.uuid)) }
            )
        }

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.transaction_transaction_date),
            value = dto.transactionDate.fromISO().toShortDate()
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.transaction_creation_date),
            value = dto.creationDate.fromISO().toShortDate()
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.transaction_author),
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
