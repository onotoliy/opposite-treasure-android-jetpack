package com.github.onotoliy.opposite.treasure.ui.activity

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.model.EventActivityModel
import com.github.onotoliy.opposite.treasure.ui.IconEdit
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.DepositPageViewVO
import com.github.onotoliy.opposite.treasure.ui.views.EventView
import com.github.onotoliy.opposite.treasure.ui.views.TransactionPageViewVO
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.observe
import com.github.onotoliy.opposite.treasure.utils.pk
import javax.inject.Inject

class EventActivity : AppCompatActivity() {

    @Inject lateinit var model: EventActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        model.loading(intent.pk ?: throw IllegalArgumentException())

        setContent {
            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            icon = { IconEdit() },
                            onClick = { navigateTo(Screen.EventEditScreen(intent.pk)) }
                        )
                    },
                    bodyContent = { EventScreen(model, ::navigateTo) },
                    navigateTo = ::navigateTo
                )
            }
        }
    }

}

enum class EventTab(val label: String) {
    GENERAL(Resources.getSystem().getString(R.string.event_tab_general)),
    DEBTORS(Resources.getSystem().getString(R.string.event_tab_debtor)),
    TRANSACTIONS(Resources.getSystem().getString(R.string.event_tab_transaction))
}

@Composable
fun EventScreen(model: EventActivityModel, navigateTo: (Screen) -> Unit) {
    val selected = remember { mutableStateOf(EventTab.GENERAL) }

    model.pending.observe()?.let { pending ->
        Column {
            TabRow(
                selectedTabIndex = selected.value.ordinal
            ) {
                EventTab.values().forEach { tab ->
                    Tab(
                        selected = tab == selected.value,
                        onClick = { selected.value = tab },
                        text = { Text(text = tab.label) }
                    )
                }
            }

            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            when (selected.value) {
                EventTab.GENERAL -> model.event.observe()?.let {
                    EventView(data = it, navigateTo = navigateTo)
                }
                EventTab.DEBTORS ->
                    DepositPageViewVO(
                        view = model.debtors,
                        navigateTo = navigateTo,
                        navigateToNextPageScreen = { offset, numberOrRows, _ ->
                            model.nextDepositPageLoading(offset, numberOrRows)
                        }
                    )

                EventTab.TRANSACTIONS ->
                    TransactionPageViewVO(
                        view = model.transactions,
                        navigateTo = navigateTo,
                        navigateToNextPageScreen = { offset, numberOrRows, _ ->
                            model.nextTransactionPageLoading(offset, numberOrRows)
                        }
                    )
            }
        }
    }
}
