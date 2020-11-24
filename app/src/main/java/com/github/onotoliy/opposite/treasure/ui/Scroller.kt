package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.IconRefresh
import com.github.onotoliy.opposite.treasure.PageView
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.size

@Composable
fun <D> Scroller(
    view: PageView<D>,
    navigateToNextPageScreen: () -> Unit = {},
    itemView: @Composable() (D) -> Unit
) {
    view.context?.let { page ->
        ScrollableColumn(
            scrollState = view.scrollerPosition,
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 50.dp)
        ) {
            page.context.forEachIndexed { index, item ->
                itemView(item)
                if (index == page.size - 1 && page.meta.total > page.size) {
                    IconButton(
                        onClick = { navigateToNextPageScreen() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            IconRefresh()
                            Text(text = stringResource(id = R.string.treasure_scroller_next_page))
                        }
                    }
                }
            }
        }
    }
}
