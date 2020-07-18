package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.ScrollerPosition
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.*
import androidx.ui.material.Divider
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.LocationOn
import androidx.ui.res.stringResource
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.formatDate
import com.github.onotoliy.opposite.treasure.ui.typography

@Preview
@Composable
fun EventPageScreenPreview() {
    val list = listOf(
        Event(
            uuid = "1234567890",
            name = "Открытие мотосезона город Улан-Удэ 2020 год",
            contribution = "1 000",
            deadline = "20.12.2019",
            total = "10 000",
            creationDate = "20.12.2019",
            author = Option(uuid = "1234567890", name = "Анатолий Похресный")
        )
    )

    EventPageScreen(
        page = Page(Meta(), list),
        scrollerPosition = ScrollerPosition(),
        navigateTo = { }
    )
}

@Composable
fun EventPageScreen(
    model: Screen.EventPageScreen,
    navigateTo: (Screen) -> Unit
) {
    model.page?.let {
        EventPageScreen(
            page = it,
            scrollerPosition = model.scrollerPosition,
            navigateTo = navigateTo
        )
    }
}

@Composable
private fun EventPageScreen(
    page: Page<Event>,
    scrollerPosition: ScrollerPosition,
    navigateTo: (Screen) -> Unit
) {
    TreasureScroller(
        page = page,
        scrollerPosition = scrollerPosition,
        navigateToNextPageScreen = {
            navigateTo(
                Screen.EventPageScreen(
                    offset = page.context.size,
                    numberOfRows = page.meta.paging.size,
                    default = page
                )
            )
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
                    Icon(
                        asset = Icons.Filled.LocationOn,
                        modifier = Modifier.padding(0.dp, 0.dp, 6.dp, 0.dp)
                    )
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
