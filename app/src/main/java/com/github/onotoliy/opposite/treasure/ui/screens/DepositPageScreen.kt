package com.github.onotoliy.opposite.treasure.ui.screens

import android.accounts.AccountManager
import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.*
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.AccountCircle
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.auth.getDepositPage
import com.github.onotoliy.opposite.treasure.ui.typography
import java.util.*

@Preview
@Composable
fun DepositPageScreenPreview() {
    DepositPageScreen(deposits = state { Page(Meta(), listOf(
        Deposit(Option(UUID.randomUUID().toString(), "Иванов Иван Иванович"), "10000"),
        Deposit(Option(UUID.randomUUID().toString(), "Петров Иван Иванович"), "10000"),
        Deposit(Option(UUID.randomUUID().toString(), "Сидоров Иван Иванович"), "10000"),
        Deposit(Option(UUID.randomUUID().toString(), "Курочкин Иван Иванович"), "10000"),
        Deposit(Option(UUID.randomUUID().toString(), "Петухов Иван Иванович"), "10000")
    )) }) {

    }
}

@Composable
fun DepositPageScreen(manager: AccountManager, navigateTo: (Screen) -> Unit) {
    DepositPageScreen(
        deposits = state { manager.getDepositPage() },
        navigateTo = navigateTo
    )
}

@Composable
fun DepositPageScreen(deposits: MutableState<Page<Deposit>>, navigateTo: (Screen) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        deposits.value.context.forEach {
            Row(
                modifier = Modifier.fillMaxWidth().padding(6.dp, 3.dp).clickable(onClick = {
                    navigateTo(Screen.DepositScreen(it.uuid))
                }),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(asset = Icons.Filled.AccountCircle, modifier = Modifier.padding(0.dp, 0.dp, 6.dp, 0.dp))
                    Text(text = it.person.name, style = typography.subtitle1)
                }
                Text(text = it.deposit, style = typography.subtitle1, textAlign = TextAlign.Right)
            }
            Divider()
        }
    }
}
