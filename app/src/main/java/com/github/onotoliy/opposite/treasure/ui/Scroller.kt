package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.IconButton
import androidx.ui.res.stringResource
import androidx.ui.unit.dp
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
        VerticalScroller(
            scrollerPosition = view.scrollerPosition,
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
