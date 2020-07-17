package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.Column
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.formatDate
import com.github.onotoliy.opposite.treasure.models.TransactionScreenModel
import com.github.onotoliy.opposite.treasure.ui.typography

@Preview
@Composable
fun TransactionScreenPreview() {
    TransactionScreen(
        data =
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
    ) {

    }
}

@Composable
fun TransactionScreen(model: TransactionScreenModel, navigateTo: (Screen) -> Unit) {
    TransactionScreen(data = model.transaction, navigateTo = navigateTo)
}

@Composable
private fun TransactionScreen(data: Transaction?, navigateTo: (Screen) -> Unit) {
    Column {
        Text(
            text = stringResource(id = R.string.transaction_name),
            style = typography.h6
        )
        Text(text = data?.name?: "", style = typography.body1)
        Text(
            text = stringResource(id = R.string.transaction_type),
            style = typography.h6
        )
        Text(text = data?.type?.label?: "", style = typography.body1)
        Text(
            text = stringResource(id = R.string.transaction_cash),
            style = typography.h6
        )
        Text(text = data?.cash?: "", style = typography.body1)

        data?.event?.let {
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
        data?.person?.let {
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
        Text(text = data?.creationDate.formatDate(), style = typography.body1)
        Text(text = stringResource(id = R.string.event_author), style = typography.h6)
        Text(
            modifier = Modifier.clickable(onClick = {
                navigateTo(Screen.DepositScreen(data?.author?.uuid ?: ""))
            }),
            text = data?.author?.name ?: "",
            style = typography.body1
        )
    }
}