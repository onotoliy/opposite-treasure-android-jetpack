package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.models.DepositScreenModel
import com.github.onotoliy.opposite.treasure.ui.typography

@Preview
@Composable
fun DepositScreenPreview() {
    DepositScreen(
        deposit = Deposit(Option("890--", "Анатолий Похресный")),
        cashbox = Cashbox("1000.00", "2020-12-31")
    )
}

@Composable
fun DepositScreen(model: DepositScreenModel) {
    DepositScreen(deposit = model.deposit, cashbox = model.cashbox)
}

@Composable
private fun DepositScreen(deposit: Deposit?, cashbox: Cashbox?) {
    Column {
        Text(text = stringResource(id = R.string.deposit_person), style = typography.h6)
        Text(text = deposit?.person?.name ?: "", style = typography.body1)
        Text(text = stringResource(id = R.string.deposit_contribution), style = typography.h6)
        Text(text = deposit?.deposit ?: "", style = typography.body1)
        Text(text = stringResource(id = R.string.deposit_cashbox), style = typography.h6)
        Text(text = cashbox?.deposit ?: "", style = typography.body1)
        Text(text = stringResource(id = R.string.deposit_last_update_date), style = typography.h6)
        Text(text = cashbox?.lastUpdateDate ?: "", style = typography.body1)
    }
}
