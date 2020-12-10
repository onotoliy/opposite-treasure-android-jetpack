package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.ui.H6
import com.github.onotoliy.opposite.treasure.ui.IconAccountCircle
import com.github.onotoliy.opposite.treasure.ui.Scroller
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage

@Composable
fun DepositPageViewVO(
    view: LiveDataPage<DepositVO>,
    navigateTo: (Screen) -> Unit,
    navigateToNextPageScreen: (Int, Int, LiveDataPage<DepositVO>?) -> Unit
) {
    Scroller(
        page = view,
        navigateToNextPageScreen = {
            navigateToNextPageScreen(view.offset, view.numberOfRows, view)
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
                Text(text = it.name, style = H6)
            }
            Text(text = it.deposit, style = H6, textAlign = TextAlign.Right)
        }
        Divider()
    }
}
