package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.ui.BODY_GREY
import com.github.onotoliy.opposite.treasure.ui.H6
import com.github.onotoliy.opposite.treasure.ui.H6_BOLD
import com.github.onotoliy.opposite.treasure.ui.IconDown
import com.github.onotoliy.opposite.treasure.ui.IconUp
import com.github.onotoliy.opposite.treasure.ui.Scroller
import com.github.onotoliy.opposite.treasure.utils.LiveDataPage
import com.github.onotoliy.opposite.treasure.utils.fromISO
import com.github.onotoliy.opposite.treasure.utils.toShortDate

@Composable
fun TransactionPageViewVO(
    view: LiveDataPage<TransactionVO>,
    navigateTo: (Screen) -> Unit,
    navigateToNextPageScreen: (Int, Int, LiveDataPage<TransactionVO>?) -> Unit
)  {
    Scroller(
        page = view,
        navigateToNextPageScreen = {
            navigateToNextPageScreen(view.offset, view.numberOfRows, view)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(6.dp, 3.dp).clickable(onClick = {
                navigateTo(Screen.TransactionScreen(it.uuid))
            })
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.weight(4f)) {
                    when (it.type) {
                        TransactionType.NONE, TransactionType.WRITE_OFF -> IconDown()
                        TransactionType.COST, TransactionType.PAID -> IconDown()
                        TransactionType.CONTRIBUTION, TransactionType.EARNED -> IconUp()
                    }
                    Text(
                        text = it.name,
                        softWrap = false,
                        style = H6_BOLD
                    )
                }
                Text(
                    modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp),
                    text = it.cash,
                    style = H6,
                    textAlign = TextAlign.Right
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = it.person?.name ?: "", style = BODY_GREY)
                Text(text = it.creationDate.fromISO().toShortDate(), style = BODY_GREY)
            }
            Divider()
        }
    }
}
