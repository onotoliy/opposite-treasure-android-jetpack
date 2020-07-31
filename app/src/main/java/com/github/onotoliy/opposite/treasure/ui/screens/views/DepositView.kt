package com.github.onotoliy.opposite.treasure.ui.screens.views

import androidx.compose.Composable
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.res.stringResource
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.ui.typography

@Composable
fun DepositView(deposit: Deposit?, cashbox: Cashbox?) {
    deposit?.let {
        cashbox?.let {
            Column {
                Text(text = stringResource(id = R.string.deposit_person), style = typography.h6)
                Text(text = deposit.person.name, style = typography.body1)
                Text(
                    text = stringResource(id = R.string.deposit_contribution),
                    style = typography.h6
                )
                Text(text = deposit.deposit, style = typography.body1)
                Text(text = stringResource(id = R.string.deposit_cashbox), style = typography.h6)
                Text(text = cashbox.deposit, style = typography.body1)
                Text(
                    text = stringResource(id = R.string.deposit_last_update_date),
                    style = typography.h6
                )
                Text(text = cashbox.lastUpdateDate, style = typography.body1)
            }
        }
    }
}
