package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.IconButton
import androidx.ui.material.LinearProgressIndicator
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Refresh
import androidx.ui.res.stringResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.R

@Composable
fun <D> TreasureScroller(
    page: Page<D>,
    navigateToNextPageScreen: () -> Unit = {},
    scrollerPosition: ScrollerPosition = ScrollerPosition(),
    itemView: @Composable() (D) -> Unit
) {
    TreasureScroller(
        data = page.context,
        total = page.meta.total,
        navigateToNextPageScreen = navigateToNextPageScreen,
        scrollerPosition = scrollerPosition,
        itemView = itemView
    )
}

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
