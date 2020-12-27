package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R

@Composable
fun <D> Scroller(
    list: List<D>,
    total: Long,
    navigateToNextPageScreen: () -> Unit,
    content: @Composable() (D) -> Unit
) {
    ScrollableColumn(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 50.dp)
    ) {
        list.forEachIndexed { index, item ->
            Column(modifier = Modifier.fillMaxWidth().padding(6.dp, 10.dp)) {
                content(item)
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