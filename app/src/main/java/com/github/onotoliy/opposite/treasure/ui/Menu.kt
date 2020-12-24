package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.emptyContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen

@Composable
fun Menu(
    bodyContent: @Composable (PaddingValues) -> Unit,
    navigateTo: (Screen) -> Unit = {},
    floatingActionButton: @Composable () -> Unit = emptyContent(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { androidx.compose.material.Text(text = stringResource(id = R.string.app_name)) },
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
                        onClick = { navigateTo(Screen.DepositScreen()) }
                    ) {
                         IconHome()
                    }
                    IconButton(
                        onClick = { navigateTo(Screen.DepositPageScreen) }) {
                        IconDeposits()
                    }
                    IconButton(
                        onClick = { navigateTo(Screen.TransactionPageScreen) }) {
                        IconTransactions()
                    }
                    IconButton(
                        onClick = { navigateTo(Screen.EventPageScreen) }) {
                        IconEvents()
                    }
                }
            }
        }
    )
}
