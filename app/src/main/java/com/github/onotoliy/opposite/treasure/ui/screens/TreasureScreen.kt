package com.github.onotoliy.opposite.treasure.ui.screens

import android.accounts.AccountManager
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.animation.Crossfade
import androidx.ui.material.Surface
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.auth.*
import com.github.onotoliy.opposite.treasure.ui.Menu

@Composable
fun TreasureScreen(firstScreen: Screen, manager: AccountManager) {
    val state = state {
        firstScreen.loading(manager)
        firstScreen
    }
    val navigateTo: (Screen) -> Unit = {
        state.value = it
        state.value.loading(manager)
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
                        bodyContent = { HomeScreen(screen) },
                        navigateTo = navigateTo
                    )
                is Screen.DepositScreen ->
                    Menu(
                        bodyContent = { DepositScreen(screen) },
                        navigateTo = navigateTo
                    )
                is Screen.EventScreen ->
                    Menu(
                        bodyContent = { EventScreen(screen, navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.TransactionScreen ->
                    Menu(
                        bodyContent = { TransactionScreen(screen, navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.DepositPageScreen ->
                    Menu(
                        bodyContent = { DepositPageScreen(screen, navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.EventPageScreen ->
                    Menu(
                        bodyContent = { EventPageScreen(screen, navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.TransactionPageScreen ->
                    Menu(
                        bodyContent = { TransactionPageScreen(screen, navigateTo) },
                        navigateTo = navigateTo
                    )
            }
        }
    }
}
