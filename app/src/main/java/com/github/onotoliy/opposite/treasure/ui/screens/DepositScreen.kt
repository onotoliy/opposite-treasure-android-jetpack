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
import androidx.compose.runtime.state
import androidx.compose.ui.Modifier
import com.github.onotoliy.opposite.treasure.PageView
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.observe
import com.github.onotoliy.opposite.treasure.ui.screens.views.DepositView
import com.github.onotoliy.opposite.treasure.ui.screens.views.EventPageView
import com.github.onotoliy.opposite.treasure.ui.screens.views.TransactionPageView

enum class DepositTab(val label: String) {
    GENERAL("Общее"),
    DEBT("Долги"),
    TRANSACTION("Операции")
}

@Composable
fun DepositScreen(
    model: Screen.DepositScreen,
    navigateTo: (Screen) -> Unit
) {/*
    val selected = remember { mutableStateOf(model.tab) }

    model.pending.observe()?.let { pending ->
        Column {
            TabRow(
                selectedTabIndex = selected.value.ordinal
            ) {
                DepositTab.values().forEach { tab ->
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
                DepositTab.GENERAL -> DepositView(
                    deposit = model.deposit.observe(),
                    cashbox = model.cashbox.observe()
                )
                DepositTab.DEBT -> model.events.observe()?.let {
                    EventPageView(
                        view = it,
                        navigateTo = navigateTo,
                        navigateToNextPageScreen = { offset, numberOrRows, default ->
                            navigateTo(
                                model.copy(
                                    tab = DepositTab.DEBT,
                                    ep = PageView(
                                        offset = offset,
                                        numberOfRows = numberOrRows,
                                        default = default
                                    )
                                )
                            )
                        }
                    )
                }
                DepositTab.TRANSACTION -> model.transactions.observe()?.let {
                    TransactionPageView(
                        view = it,
                        navigateTo = navigateTo,
                        navigateToNextPageScreen = { offset, numberOrRows, default ->
                            navigateTo(
                                model.copy(
                                    tab = DepositTab.TRANSACTION,
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
