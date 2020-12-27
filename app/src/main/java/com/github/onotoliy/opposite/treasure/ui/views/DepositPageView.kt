package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.ui.H6
import com.github.onotoliy.opposite.treasure.ui.IconAccountCircle
import com.github.onotoliy.opposite.treasure.ui.Scroller

@Composable
fun DepositPageView(
    list: List<DepositVO>,
    total: Long,
    navigateTo: (Screen) -> Unit,
    navigateToNextPageScreen: () -> Unit
) {
    Scroller(
        list = list,
        total = total,
        navigateToNextPageScreen = navigateToNextPageScreen
    ) {
        DepositItemView(it, navigateTo)
    }
}

@Composable
fun DepositItemView(dto: DepositVO, navigateTo: (Screen) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(6.dp, 3.dp).clickable(onClick = {
            navigateTo(Screen.DepositScreen(dto.uuid))
        }),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            IconAccountCircle()
            Text(text = dto.name, style = H6)
        }
        Text(text = dto.deposit, style = H6, textAlign = TextAlign.Right)
    }
    Divider()
}
