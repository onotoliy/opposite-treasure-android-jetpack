package com.github.onotoliy.opposite.treasure.ui.screens.views

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.*
import androidx.ui.material.Divider
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.ui.Scroller
import com.github.onotoliy.opposite.treasure.ui.typography

@Composable
fun TransactionPageView(
    view: PageView<Transaction>,
    navigateTo: (Screen) -> Unit,
    navigateToNextPageScreen: (Int, Int, Page<Transaction>?) -> Unit
)  {
    Scroller(
        view = view,
        navigateToNextPageScreen = {
            navigateToNextPageScreen(view.context.offset, view.context.numberOfRows, view.context)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(6.dp, 3.dp).clickable(onClick = {
                navigateTo(Screen.TransactionScreen(it.uuid))
            })
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.weight(4f)) {
                    when (it.type) {
                        TransactionType.NONE, TransactionType.WRITE_OFF -> IconDown()
                        TransactionType.COST, TransactionType.PAID -> IconDown()
                        TransactionType.CONTRIBUTION, TransactionType.EARNED -> IconUp()
                    }
                    Text(
                        text = it.name,
                        softWrap = false,
                        style = typography.subtitle1
                    )
                }
                Text(
                    modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp),
                    text = it.cash,
                    style = typography.subtitle1,
                    textAlign = TextAlign.Right
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = it.person?.name ?: "", style = typography.subtitle2)
                Text(text = it.creationDate.formatDate(), style = typography.subtitle2)
            }
            Divider()
        }
    }
}
