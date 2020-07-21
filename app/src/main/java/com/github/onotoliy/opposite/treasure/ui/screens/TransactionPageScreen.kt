package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.ScrollerPosition
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.*
import androidx.ui.material.Divider
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.KeyboardArrowDown
import androidx.ui.material.icons.filled.KeyboardArrowRight
import androidx.ui.material.icons.filled.KeyboardArrowUp
import androidx.ui.res.stringResource
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.TransactionType.*
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.formatDate
import com.github.onotoliy.opposite.treasure.ui.typography

@Preview
@Composable
fun TransactionPageScreenPreview() {
    val list = listOf(
        Transaction(
            uuid = "1234567890",
            name = "Открытие мотосезона город Улан-Удэ 2020 год",
            cash = "1 000",
            type = CONTRIBUTION,
            event = null,
            creationDate = "20.12.2019",
            author = Option(uuid = "1234567890", name = "Анатолий Похресный"),
            person = Option(uuid = "1234567890", name = "Анатолий Похресный")
        )
    )

    TransactionPageScreen(
        page = Page(Meta(), list),
        scrollerPosition = ScrollerPosition(),
        navigateTo = { }
    )
}

@Composable
fun TransactionPageScreen(
    model: Screen.TransactionPageScreen,
    navigateTo: (Screen) -> Unit
) {
    model.page?.let {
        if (it.context.isNullOrEmpty()) {
            ProgressScreen()
        } else {
            TransactionPageScreen(
                page = it,
                scrollerPosition = model.scrollerPosition,
                navigateTo = navigateTo
            )
        }
    }
}

@Composable
private fun TransactionPageScreen(
    page: Page<Transaction>,
    scrollerPosition: ScrollerPosition,
    navigateTo: (Screen) -> Unit
) {
    TreasureScroller(
        page = page,
        scrollerPosition = scrollerPosition,
        navigateToNextPageScreen = {
            navigateTo(
                Screen.TransactionPageScreen(
                    offset = page.context.size,
                    numberOfRows = page.meta.paging.size,
                    default = page
                )
            )
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
                    Icon(
                        asset = when (it.type) {
                            NONE, WRITE_OFF -> Icons.Filled.KeyboardArrowRight
                            COST, PAID -> Icons.Filled.KeyboardArrowDown
                            CONTRIBUTION, EARNED -> Icons.Filled.KeyboardArrowUp
                        },
                        modifier = Modifier.padding(0.dp, 0.dp, 6.dp, 0.dp)
                    )
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
                Row {
                    Text(
                        text = stringResource(id = R.string.transaction_page_creation_date),
                        style = typography.subtitle2
                    )
                    Text(
                        text = it.creationDate.formatDate(),
                        style = typography.subtitle2
                    )
                }

                Row {
                    Text(
                        text = stringResource(R.string.transaction_page_person),
                        style = typography.subtitle2
                    )
                    Text(
                        text = it.person?.name ?: "",
                        style = typography.subtitle2
                    )
                }
            }
            Divider()
        }
    }
}
