package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.ui.BODY
import com.github.onotoliy.opposite.treasure.ui.H6_BOLD

@Composable
fun DepositView(deposit: Deposit?, cashbox: Cashbox?) {
    deposit?.let {
        cashbox?.let {
            Column {
                Text(text = stringResource(id = R.string.deposit_person), style = H6_BOLD)
                Text(text = deposit.name, style = BODY)

                Text(text = stringResource(id = R.string.deposit_contribution), style = H6_BOLD)
                Text(text = deposit.deposit, style = BODY)

                Text(text = stringResource(id = R.string.deposit_cashbox), style = H6_BOLD)
                Text(text = cashbox.deposit, style = BODY)

                Text(text = stringResource(id = R.string.deposit_last_update_date), style = H6_BOLD)
                Text(text = cashbox.lastUpdateDate, style = BODY)
            }
        }
    }
}
