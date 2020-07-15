package com.github.onotoliy.opposite.treasure.ui.screens

import android.accounts.AccountManager
import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.ScrollerPosition
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.layout.*
import androidx.ui.material.Divider
import androidx.ui.material.MaterialTheme
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.LocationOn
import androidx.ui.res.stringResource
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.auth.getEventPage
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
        events = list,
        offset = list.size,
        total = 2,
        numberOfRows = 10,
        navigateTo = { }
    )
}

@Composable
fun EventPageScreen(
    manager: AccountManager,
    navigateTo: (Screen) -> Unit,
    offset: Int = 0,
    numberOfRows: Int = 20,
    default: List<Event> = listOf()
) {
    val page = manager.getEventPage(offset, numberOfRows)
    val list = mutableListOf<Event>()

    list.addAll(default)
    list.addAll(page.context)

    EventPageScreen(
        events = list,
        offset = list.size,
        total = page.meta.total,
        numberOfRows = numberOfRows,
        navigateTo = navigateTo,
        scrollerPosition = if (default.size < 10) ScrollerPosition() else ScrollerPosition((100 * default.size).toFloat())
    )
}

@Composable
private fun EventPageScreen(
    events: List<Event>,
    total: Int,
    offset: Int = 0,
    numberOfRows: Int = 20,
    navigateTo: (Screen) -> Unit,
    scrollerPosition: ScrollerPosition = ScrollerPosition()
) {
    TreasureScroller(
        data = events,
        total = total,
        scrollerPosition = scrollerPosition,
        navigateToNextPageScreen = {
            Screen.EventPageScreen(
                offset = offset,
                numberOfRows = numberOfRows,
                default = events
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
