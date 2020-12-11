package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.ui.BODY
import com.github.onotoliy.opposite.treasure.ui.H6_BOLD
import com.github.onotoliy.opposite.treasure.utils.fromISO
import com.github.onotoliy.opposite.treasure.utils.toShortDate

@Composable
fun TransactionView(data: TransactionVO, navigateTo: (Screen) -> Unit) {
    Column {
        Text(text = stringResource(id = R.string.transaction_name), style = H6_BOLD)
        Text(text = data.name, style = BODY)

        Text(text = stringResource(id = R.string.transaction_type), style = H6_BOLD)
        Text(text = data.type.label, style = BODY)

        Text(text = stringResource(id = R.string.transaction_cash), style = H6_BOLD)
        Text(text = data.cash, style = BODY)

        data.event?.let {
            Text(text = stringResource(id = R.string.transaction_event), style = H6_BOLD)
            Text(
                modifier = Modifier.clickable(onClick = {
                    navigateTo(Screen.EventScreen(it.uuid))
                }),
                text = it.name,
                style = H6_BOLD
            )
        }

        data.person?.let {
            Text(text = stringResource(id = R.string.transaction_person), style = H6_BOLD)
            Text(
                modifier = Modifier.clickable(onClick = {
                    navigateTo(Screen.DepositScreen(it.uuid))
                }),
                text = it.name,
                style = BODY
            )
        }

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