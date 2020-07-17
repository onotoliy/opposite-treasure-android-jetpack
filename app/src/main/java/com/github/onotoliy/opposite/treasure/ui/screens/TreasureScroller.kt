package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.ScrollerPosition
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.IconButton
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Refresh
import androidx.ui.res.stringResource
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R

@Composable
fun <D> TreasureScroller(
    data: List<D>?,
    total: Int,
    navigateToNextPageScreen: () -> Unit = {},
    scrollerPosition: ScrollerPosition = ScrollerPosition(),
    itemView: @Composable() (D) -> Unit
) {
    VerticalScroller(
        scrollerPosition = scrollerPosition,
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 100.dp)
    ) {
        data?.forEachIndexed { index, item ->
            itemView(item)

            if (index == data.size - 1 && total > data.size) {
                IconButton(
                    onClick = { navigateToNextPageScreen() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row {
                        Icon(asset = Icons.Filled.Refresh)
                        Text(text = stringResource(id = R.string.treasure_scroller_next_page))
                    }
                }
            }
        }
    }
}
