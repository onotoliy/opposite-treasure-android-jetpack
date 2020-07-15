package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.material.*
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Home
import androidx.ui.material.icons.filled.List
import androidx.ui.material.icons.filled.LocationOn
import androidx.ui.material.icons.filled.Person
import androidx.ui.res.stringResource
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen

@Composable
fun Menu(
    bodyContent: @Composable() (Modifier) -> Unit,
    navigateTo: (Screen) -> Unit = {},
    scaffoldState: ScaffoldState = ScaffoldState()
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topAppBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        },
        bodyContent = bodyContent,
        bottomAppBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { navigateTo(Screen.HomeScreen) },
                        icon = { Icon(asset = Icons.Filled.Home) }
                    )
                    IconButton(
                        onClick = { navigateTo(Screen.DepositPageScreen) },
                        icon = { Icon(asset = Icons.Filled.Person) }
                    )
                    IconButton(
                        onClick = { navigateTo(Screen.TransactionPageScreen()) },
                        icon = { Icon(asset = Icons.Filled.List) }
                    )
                    IconButton(
                        onClick = { navigateTo(Screen.EventPageScreen()) },
                        icon = { Icon(asset = Icons.Filled.LocationOn) }
                    )
                }
            }
        }
    )
}
