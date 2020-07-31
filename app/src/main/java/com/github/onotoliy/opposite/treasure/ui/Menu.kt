package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.material.*
import androidx.ui.res.stringResource
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.R

@Composable
fun Menu(
    bodyContent: @Composable() (Modifier) -> Unit,
    navigateTo: (Screen) -> Unit = {},
    floatingActionButton: @Composable (() -> Unit)? = null,
    scaffoldState: ScaffoldState = ScaffoldState()
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topAppBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        },
        floatingActionButton = floatingActionButton,
        bodyContent = bodyContent,
        bottomAppBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { navigateTo(Screen.DepositScreen()) },
                        icon = { IconHome() }
                    )
                    IconButton(
                        onClick = { navigateTo(Screen.DepositPageScreen()) },
                        icon = { IconDeposits() }
                    )
                    IconButton(
                        onClick = { navigateTo(Screen.TransactionPageScreen()) },
                        icon = { IconTransactions() }
                    )
                    IconButton(
                        onClick = { navigateTo(Screen.EventPageScreen()) },
                        icon = { IconEvents() }
                    )
                }
            }
        }
    )
}
