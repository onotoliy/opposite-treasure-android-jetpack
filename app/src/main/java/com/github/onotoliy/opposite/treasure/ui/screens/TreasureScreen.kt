package com.github.onotoliy.opposite.treasure.ui.screens

import android.accounts.AccountManager
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.animation.Crossfade
import androidx.ui.material.Surface
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.auth.addAccount
import com.github.onotoliy.opposite.treasure.ui.Menu

@Composable
fun TreasureScreen(firstScreen: Screen, manager: AccountManager) {
    val state = state { firstScreen }

    val navigateTo: (Screen) -> Unit = {
        state.value = it
    }

    Crossfade(state.value) { screen ->
        Surface {
            when (screen) {
                is Screen.LoginScreen -> LoginScreen { account, password, token ->
                    manager.addAccount(account, password, token)
                    navigateTo(Screen.HomeScreen)
                }
                is Screen.HomeScreen ->
                    Menu(
                        bodyContent = { DepositScreen(manager) },
                        navigateTo = navigateTo
                    )
                is Screen.DepositScreen ->
                    Menu(
                        bodyContent = { DepositScreen(manager, uuid = screen.deposit) },
                        navigateTo = navigateTo
                    )
                is Screen.EventScreen ->
                    Menu(
                        bodyContent = { EventScreen(manager, uuid = screen.event, navigateTo = navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.TransactionScreen ->
                    Menu(
                        bodyContent = { TransactionScreen(manager, uuid = screen.transaction, navigateTo = navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.DepositPageScreen ->
                    Menu(
                        bodyContent = { DepositPageScreen(manager, navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.EventPageScreen ->
                    Menu(
                        bodyContent = { EventPageScreen(manager, navigateTo, screen.offset, screen.numberOfRows, screen.default) },
                        navigateTo = navigateTo
                    )
                is Screen.TransactionPageScreen ->
                    Menu(
                        bodyContent = { TransactionPageScreen(manager, navigateTo, screen.offset, screen.numberOfRows, screen.default) },
                        navigateTo = navigateTo
                    )
            }
        }
    }
}
