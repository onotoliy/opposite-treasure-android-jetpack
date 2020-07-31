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
) {
    val selected = state { model.tab }

    model.pending.observe()?.let { pending ->
        Column {
            TabRow(
                items = DepositTab.values().toList(),
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
    }
}
