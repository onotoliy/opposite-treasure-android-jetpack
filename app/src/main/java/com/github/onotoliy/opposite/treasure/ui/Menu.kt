package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.emptyContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.Screen.DepositPageScreen
import com.github.onotoliy.opposite.treasure.Screen.DepositScreen
import com.github.onotoliy.opposite.treasure.Screen.EventPageScreen
import com.github.onotoliy.opposite.treasure.Screen.TransactionPageScreen

@Composable
fun Menu(
    screen: Screen,
    actions: @Composable RowScope.() -> Unit = {},
    bodyContent: @Composable (PaddingValues) -> Unit,
    navigateTo: (Screen) -> Unit = {},
    floatingActionButton: @Composable () -> Unit = emptyContent(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                backgroundColor = MaterialTheme.colors.surface,
                actions = actions
            )
        },
        floatingActionButton = floatingActionButton,
        bodyContent = bodyContent,
        bottomBar = {
            BottomAppBar(backgroundColor = MaterialTheme.colors.surface) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { navigateTo(DepositScreen()) },
                    ) {
                         IconHome(
                             tint = if (screen is DepositScreen) MaterialTheme.colors.primary else Color.Black
                         )
                    }
                    IconButton(
                        onClick = { navigateTo(DepositPageScreen) }) {
                        IconDeposits(
                            tint = if (screen is DepositPageScreen) MaterialTheme.colors.primary else Color.Black
                        )
                    }
                    IconButton(
                        onClick = { navigateTo(TransactionPageScreen) }) {
                        IconTransactions(
                            tint = if (screen is TransactionPageScreen) MaterialTheme.colors.primary else Color.Black
                        )
                    }
                    IconButton(
                        onClick = { navigateTo(EventPageScreen) }) {
                        IconEvents(
                            tint = if (screen is EventPageScreen) MaterialTheme.colors.primary else Color.Black
                        )
                    }
                }
            }
        }
    )
}
