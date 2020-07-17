package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.*
import androidx.ui.material.Divider
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.AccountCircle
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.models.DepositScreenPageModel
import com.github.onotoliy.opposite.treasure.ui.typography
import java.util.*

@Preview
@Composable
fun DepositPageScreenPreview() {
    DepositPageScreen(
        deposits = listOf(
            Deposit(Option(UUID.randomUUID().toString(), "Иванов Иван Иванович"), "10000"),
            Deposit(Option(UUID.randomUUID().toString(), "Петров Иван Иванович"), "10000"),
            Deposit(Option(UUID.randomUUID().toString(), "Сидоров Иван Иванович"), "10000"),
            Deposit(Option(UUID.randomUUID().toString(), "Курочкин Иван Иванович"), "10000"),
            Deposit(Option(UUID.randomUUID().toString(), "Петухов Иван Иванович"), "10000")
        )
    ) {

    }
}

@Composable
fun DepositPageScreen(model: DepositScreenPageModel, navigateTo: (Screen) -> Unit) {
    model.deposits?.let {
        DepositPageScreen(
            deposits = it,
            navigateTo = navigateTo
        )
    }
}

@Composable
fun DepositPageScreen(deposits: List<Deposit>, navigateTo: (Screen) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        deposits.forEach {
            Row(
                modifier = Modifier.fillMaxWidth().padding(6.dp, 3.dp).clickable(onClick = {
                    navigateTo(Screen.DepositScreen(it.uuid))
                }),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        asset = Icons.Filled.AccountCircle,
                        modifier = Modifier.padding(0.dp, 0.dp, 6.dp, 0.dp)
                    )
                    Text(text = it.person.name, style = typography.subtitle1)
                }
                Text(text = it.deposit, style = typography.subtitle1, textAlign = TextAlign.Right)
            }
            Divider()
        }
    }
}
