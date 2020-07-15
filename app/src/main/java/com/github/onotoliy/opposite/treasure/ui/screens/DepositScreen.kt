package com.github.onotoliy.opposite.treasure.ui.screens

import android.accounts.AccountManager
import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.material.MaterialTheme
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.auth.getCashbox
import com.github.onotoliy.opposite.treasure.auth.getDeposit
import com.github.onotoliy.opposite.treasure.auth.getUUID
import com.github.onotoliy.opposite.treasure.ui.typography

@Preview
@Composable
fun DepositScreenPreview() {
    DepositScreen(
        deposit = state { Deposit(Option("890--", "Анатолий Похресный")) },
        cashbox = state { Cashbox("1000.00", "2020-12-31") }
    )
}

@Composable
fun DepositScreen(manager: AccountManager) {
    DepositScreen(manager, manager.getUUID())
}

@Composable
fun DepositScreen(manager: AccountManager, uuid: String) {
    val deposit = state { manager.getDeposit(uuid) }
    val cashbox = state { manager.getCashbox() }

    DepositScreen(deposit = deposit, cashbox = cashbox)
}

@Composable
private fun DepositScreen(deposit: MutableState<Deposit>, cashbox: MutableState<Cashbox>) {
    Column {
        Text(text = stringResource(id = R.string.deposit_person), style = typography.h6)
        Text(text = deposit.value.person.name, style = typography.body1)
        Text(text = stringResource(id = R.string.deposit_contribution), style = typography.h6)
        Text(text = deposit.value.deposit, style = typography.body1)
        Text(text = stringResource(id = R.string.deposit_cashbox), style = typography.h6)
        Text(text = cashbox.value.deposit, style = typography.body1)
        Text(text = stringResource(id = R.string.deposit_last_update_date), style = typography.h6)
        Text(text = cashbox.value.lastUpdateDate, style = typography.body1)
    }
}