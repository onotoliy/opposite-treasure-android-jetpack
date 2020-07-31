package com.github.onotoliy.opposite.treasure.ui.screens.views

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.*
import androidx.ui.material.Divider
import androidx.ui.res.stringResource
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.ui.Scroller
import com.github.onotoliy.opposite.treasure.ui.typography

@Composable
fun EventPageView(
    view: PageView<Event>,
    navigateTo: (Screen) -> Unit,
    navigateToNextPageScreen: (Int, Int, Page<Event>?) -> Unit
) {
    Scroller(
        view = view,
        navigateToNextPageScreen = {
            navigateToNextPageScreen(view.context.offset, view.context.numberOfRows, view.context)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(6.dp, 3.dp).clickable(onClick = {
                navigateTo(Screen.EventScreen(it.uuid))
            })
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.weight(4f)) {
                    IconEvents()
                    Text(
                        text = it.name,
                        softWrap = false,
                        style = typography.subtitle1
                    )
                }
                Text(
                    modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp),
                    text = it.contribution,
                    style = typography.subtitle1,
                    textAlign = TextAlign.Right
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text(
                        text = stringResource(id = R.string.event_page_creation_date),
                        style = typography.subtitle2
                    )
                    Text(
                        text = it.creationDate.formatDate(),
                        style = typography.subtitle2
                    )
                }
                Row {
                    Text(
                        text = stringResource(R.string.event_page_deadline),
                        style = typography.subtitle2
                    )
                    Text(
                        text = it.deadline.formatDate(),
                        style = typography.subtitle2
                    )
                }
            }
            Divider()
        }
    }
}
