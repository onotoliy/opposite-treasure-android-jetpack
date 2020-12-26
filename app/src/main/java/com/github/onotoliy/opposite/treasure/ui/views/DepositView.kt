package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.ui.components.LabeledText
import com.github.onotoliy.opposite.treasure.utils.fromISO
import com.github.onotoliy.opposite.treasure.utils.toShortDate
import java.util.*

@Composable
fun DepositView(deposit: DepositVO?, cashbox: CashboxVO?) {
    ScrollableColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.deposit_person),
            value = deposit?.name ?: "Неизвестный пользователь"
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.deposit_contribution),
            value = deposit?.deposit ?: "0.0"
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.deposit_cashbox),
            value = cashbox?.deposit ?: "0.0"
        )

        LabeledText(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.deposit_last_update_date),
            value = cashbox?.lastUpdateDate?.fromISO()?.toShortDate() ?: Date().toShortDate()
        )
    }
}
