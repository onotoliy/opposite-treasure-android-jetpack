package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.ui.BODY_GREY
import com.github.onotoliy.opposite.treasure.ui.H6
import com.github.onotoliy.opposite.treasure.ui.H6_BOLD
import com.github.onotoliy.opposite.treasure.ui.IconEvents
import com.github.onotoliy.opposite.treasure.ui.Scroller
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage
import com.github.onotoliy.opposite.treasure.utils.fromISO
import com.github.onotoliy.opposite.treasure.utils.toShortDate

@Composable
fun EventPageViewVO(
    view: LiveDataPage<EventVO>,
    navigateTo: (Screen) -> Unit,
    navigateToNextPageScreen: (Int, Int, LiveDataPage<EventVO>?) -> Unit
) {
    Scroller(
        page = view,
        navigateToNextPageScreen = {
            navigateToNextPageScreen(view.offset, view.numberOfRows, view)
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(6.dp, 10.dp)) {
            EventItemView(it, navigateTo)
        }
    }
}

@Composable
fun EventItemView(dto: EventVO, navigateTo: (Screen) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { navigateTo(Screen.EventScreen(dto.uuid)) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconEvents()

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.80f),
                    text = dto.name,
                    softWrap = false,
                    style = H6_BOLD
                )
                Text(
                    text = dto.contribution,
                    softWrap = false,
                    style = H6
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text(
                        modifier = Modifier.padding(end = 5.dp),
                        text = stringResource(id = R.string.event_page_creation_date),
                        style = BODY_GREY
                    )
                    Text(
                        text = dto.deadline.fromISO().toShortDate(),
                        style = BODY_GREY,
                        textAlign = TextAlign.Right
                    )
                }
                Row {
                    Text(
                        modifier = Modifier.padding(end = 5.dp),
                        text = stringResource(id = R.string.event_page_deadline),
                        style = BODY_GREY
                    )
                    Text(
                        text = dto.deadline.fromISO().toShortDate(),
                        style = BODY_GREY,
                        textAlign = TextAlign.Right
                    )
                }
            }
        }
    }
}