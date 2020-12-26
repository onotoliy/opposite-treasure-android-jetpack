package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.ui.BODY_GREY
import com.github.onotoliy.opposite.treasure.ui.H6
import com.github.onotoliy.opposite.treasure.ui.H6_BOLD
import com.github.onotoliy.opposite.treasure.ui.IconDown
import com.github.onotoliy.opposite.treasure.ui.IconUp
import com.github.onotoliy.opposite.treasure.ui.Scroller
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage
import com.github.onotoliy.opposite.treasure.utils.fromISO
import com.github.onotoliy.opposite.treasure.utils.toShortDate

@Composable
fun TransactionPageViewVO(
    view: LiveDataPage<TransactionVO>,
    navigateTo: (Screen) -> Unit,
    navigateToNextPageScreen: (Int, Int, LiveDataPage<TransactionVO>?) -> Unit
) {
    Scroller(
        page = view,
        navigateToNextPageScreen = {
            navigateToNextPageScreen(view.offset, view.numberOfRows, view)
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(6.dp, 10.dp)) {
            TransactionItemView(dto = it, navigateTo = navigateTo)
        }
    }
}

@Composable
fun TransactionItemView(dto: TransactionVO, navigateTo: (Screen) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateTo(Screen.TransactionScreen(dto.uuid)) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (dto.type) {
            TransactionType.NONE, TransactionType.WRITE_OFF -> IconDown()
            TransactionType.COST, TransactionType.PAID -> IconDown()
            TransactionType.CONTRIBUTION, TransactionType.EARNED -> IconUp()
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.80f),
                    text = dto.name,
                    softWrap = false,
                    style = H6_BOLD
                )
                Text(
                    text = dto.cash,
                    softWrap = false,
                    style = H6
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dto.person?.name ?: "",
                    style = BODY_GREY,
                    textAlign = TextAlign.Right
                )
                Text(
                    text = dto.transactionDate.fromISO().toShortDate(),
                    style = BODY_GREY,
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}
