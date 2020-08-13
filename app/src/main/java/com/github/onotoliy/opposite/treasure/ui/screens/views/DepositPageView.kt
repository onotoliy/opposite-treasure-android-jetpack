package com.github.onotoliy.opposite.treasure.ui.screens.views

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Divider
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.ui.H6
import com.github.onotoliy.opposite.treasure.ui.Scroller

@Composable
fun DepositPageView(
    view: PageView<Deposit>,
    navigateTo: (Screen) -> Unit,
    navigateToNextPageScreen: (Int, Int, Page<Deposit>?) -> Unit
) {
    Scroller(
        view = view,
        navigateToNextPageScreen = {
            navigateToNextPageScreen(view.context.offset, view.context.numberOfRows, view.context)
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(6.dp, 3.dp).clickable(onClick = {
                navigateTo(Screen.DepositScreen(it.uuid))
            }),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                IconAccountCircle()
                Text(text = it.person.name, style = H6)
            }
            Text(text = it.deposit, style = H6, textAlign = TextAlign.Right)
        }
        Divider()
    }
}
