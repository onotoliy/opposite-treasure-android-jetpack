package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.material.LinearProgressIndicator
import androidx.ui.material.Tab
import androidx.ui.material.TabRow
import com.github.onotoliy.opposite.treasure.PageView
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.observe
import com.github.onotoliy.opposite.treasure.ui.screens.views.*

enum class EventTab(val label: String) {
    GENERAL("Общее"),
    DEBTORS("Должники"),
    TRANSACTIONS("Операции")
}

@Composable
fun EventScreen(model: Screen.EventScreen, navigateTo: (Screen) -> Unit) {
    val selected = state { model.tab }

    model.pending.observe()?.let { pending ->
        Column {
            TabRow(
                items = EventTab.values().toList(),
                selectedIndex = selected.value.ordinal
            ) { _, tab ->
                Tab(
                    selected = tab == selected.value,
                    onSelected = { selected.value = tab },
                    text = { Text(text = tab.label) }
                )
            }

            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            when (selected.value) {
                EventTab.GENERAL -> model.event.observe()?.let {
                    EventView(data = it, navigateTo = navigateTo)
                }
                EventTab.DEBTORS -> model.debtors.observe()?.let {
                    DepositPageView(
                        view = it,
                        navigateTo = navigateTo,
                        navigateToNextPageScreen = { offset, numberOrRows, default ->
                            navigateTo(
                                model.copy(
                                    tab = EventTab.DEBTORS,
                                    dp = PageView(
                                        offset = offset,
                                        numberOfRows = numberOrRows,
                                        default = default
                                    )
                                )
                            )
                        }
                    )
                }
                EventTab.TRANSACTIONS -> model.transactions.observe()?.let {
                    TransactionPageView(
                        view = it,
                        navigateTo = navigateTo,
                        navigateToNextPageScreen = { offset, numberOrRows, default ->
                            navigateTo(
                                model.copy(
                                    tab = EventTab.TRANSACTIONS,
                                    tp = PageView(
                                        offset = offset,
                                        numberOfRows = numberOrRows,
                                        default = default
                                    )
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}