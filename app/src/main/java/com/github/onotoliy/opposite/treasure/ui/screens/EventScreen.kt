package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
fun EventScreen(model: Screen.EventScreen, navigateTo: (Screen) -> Unit) {/*
    val selected = remember { mutableStateOf(model.tab) }

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
    }*/
}