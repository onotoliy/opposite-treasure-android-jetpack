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
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage
import com.github.onotoliy.opposite.treasure.utils.observe

@Composable
fun <D> Scroller(
    page: LiveDataPage<D>,
    navigateToNextPageScreen: () -> Unit = {},
    itemView: @Composable() (D) -> Unit
) = ScrollableColumn(
    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 50.dp)
) {
    page.context.observe()?.let { view ->
        view.forEachIndexed { index, item ->
            itemView(item)

            page.total.observe()?.let { total ->
                if (index == view.size - 1 && total > view.size) {
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
