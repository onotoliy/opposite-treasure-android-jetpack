package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.R

@Composable
fun Menu(
    bodyContent: @Composable (PaddingValues) -> Unit,
    navigateTo: (Screen) -> Unit = {},
    floatingActionButton: @Composable (() -> Unit)? = null,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = { navigateTo(Screen.LoadingScreen) } ) {
                        IconCached()
                    }
                }
            )
        },
        floatingActionButton = floatingActionButton,
        bodyContent = bodyContent,
        bottomBar = {
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
                        onClick = { navigateTo(Screen.DepositPageScreen) },
                        icon = { IconDeposits() }
                    )
                    IconButton(
                        onClick = { navigateTo(Screen.TransactionPageScreen) },
                        icon = { IconTransactions() }
                    )
                    IconButton(
                        onClick = { navigateTo(Screen.EventPageScreen) },
                        icon = { IconEvents() }
                    )
                }
            }
        }
    )
}
