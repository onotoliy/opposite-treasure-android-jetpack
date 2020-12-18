package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.ui.*
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
                    if (it.local == 1) {
                        IconSmartphone(tint = Color.Red)
                    }
                    IconEvents()
                    Text(text = it.name, softWrap = false, style = H6_BOLD)
                }
                Text(
                    modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp),
                    text = it.contribution,
                    style = H6,
                    textAlign = TextAlign.Right
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text(text = stringResource(id = R.string.event_page_creation_date), style = BODY_GREY)
                    Text(text = it.creationDate.fromISO().toShortDate(), style = BODY_GREY)
                }
                Row {
                    Text(text = stringResource(R.string.event_page_deadline), style = BODY_GREY)
                    Text(text = it.deadline.fromISO().toShortDate(), style = BODY_GREY)
                }
            }

            Divider()
        }
    }
}
