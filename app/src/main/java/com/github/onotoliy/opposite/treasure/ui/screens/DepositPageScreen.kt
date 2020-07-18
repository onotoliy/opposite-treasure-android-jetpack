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
import androidx.ui.material.icons.filled.AccountCircle
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.ui.typography
import java.util.*

@Preview
@Composable
fun DepositPageScreenPreview() {
    DepositPageScreen(
        page = Page(Meta(), listOf(
            Deposit(Option(UUID.randomUUID().toString(), "Иванов Иван Иванович"), "10000"),
            Deposit(Option(UUID.randomUUID().toString(), "Петров Иван Иванович"), "10000"),
            Deposit(Option(UUID.randomUUID().toString(), "Сидоров Иван Иванович"), "10000"),
            Deposit(Option(UUID.randomUUID().toString(), "Курочкин Иван Иванович"), "10000"),
            Deposit(Option(UUID.randomUUID().toString(), "Петухов Иван Иванович"), "10000")
        )),
        scrollerPosition = ScrollerPosition()
    ) {

    }
}

@Composable
fun DepositPageScreen(model: Screen.DepositPageScreen, navigateTo: (Screen) -> Unit) {
    model.page?.let {
        DepositPageScreen(
            page = it,
            scrollerPosition = model.scrollerPosition,
            navigateTo = navigateTo
        )
    }
}

@Composable
fun DepositPageScreen(
    page: Page<Deposit>,
    scrollerPosition: ScrollerPosition,
    navigateTo: (Screen) -> Unit
) {
    TreasureScroller(
        page = page,
        scrollerPosition = scrollerPosition,
        navigateToNextPageScreen = {
            Screen.DepositPageScreen(
                offset = page.context.size,
                numberOfRows = page.meta.paging.size,
                default = page
            )
        }
    ) {
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
