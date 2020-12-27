package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.ui.H6
import com.github.onotoliy.opposite.treasure.ui.IconAccountCircle
import com.github.onotoliy.opposite.treasure.ui.IconRefresh

@Composable
fun DepositPageViewVO(
    list: List<DepositVO>,
    total: Long,
    navigateTo: (Screen) -> Unit,
    navigateToNextPageScreen: () -> Unit
) {
    ScrollableColumn(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 50.dp)
    ) {
        list.forEachIndexed { index, item ->
            Column(modifier = Modifier.fillMaxWidth().padding(6.dp, 10.dp)) {
                DepositItemView(it = item, navigateTo = navigateTo)
            }

            if (index == list.size - 1 && total > list.size) {
                IconButton(
                    onClick = { navigateToNextPageScreen() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row {
                        IconRefresh()
                        Text(
                            text = stringResource(id = R.string.treasure_scroller_next_page)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DepositItemView(it: DepositVO, navigateTo: (Screen) -> Unit) {
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
