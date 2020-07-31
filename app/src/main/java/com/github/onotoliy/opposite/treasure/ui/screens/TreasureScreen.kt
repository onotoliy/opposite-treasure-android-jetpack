package com.github.onotoliy.opposite.treasure.ui.screens

import android.accounts.AccountManager
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.animation.Crossfade
import androidx.ui.material.FloatingActionButton
import androidx.ui.material.Surface
import com.github.onotoliy.opposite.treasure.IconAdd
import com.github.onotoliy.opposite.treasure.IconEdit
import com.github.onotoliy.opposite.treasure.IconSave
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.auth.addAccount
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
                    navigateTo(Screen.DepositScreen())
                }
                is Screen.DepositScreen ->
                    Menu(
                        bodyContent = { DepositScreen(screen, navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.EventScreen ->
                    Menu(
                        floatingActionButton = {
                            FloatingActionButton(
                                icon = { IconEdit() },
                                onClick = { navigateTo(Screen.EventEditScreen(screen.pk)) }
                            )
                        },
                        bodyContent = { EventScreen(screen, navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.TransactionScreen ->
                    Menu(
                        floatingActionButton = {
                            FloatingActionButton(
                                icon = { IconEdit() },
                                onClick = { navigateTo(Screen.TransactionEditScreen(screen.pk)) }
                            )
                        },
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
                        floatingActionButton = {
                            FloatingActionButton(
                                icon = { IconAdd() },
                                onClick = { navigateTo(Screen.EventEditScreen()) }
                            )
                        },
                        bodyContent = { EventPageScreen(screen, navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.TransactionPageScreen ->
                    Menu(
                        floatingActionButton = {
                            FloatingActionButton(
                                icon = { IconAdd() },
                                onClick = { navigateTo(Screen.TransactionEditScreen()) }
                            )
                        },
                        bodyContent = { TransactionPageScreen(screen, navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.EventEditScreen ->
                    Menu(
                        floatingActionButton = {
                            FloatingActionButton(
                                icon = { IconSave() },
                                onClick = { navigateTo(Screen.EventEditScreen()) }
                            )
                        },
                        bodyContent = { EventEditScreen(screen) },
                        navigateTo = navigateTo
                    )
                is Screen.TransactionEditScreen ->
                    Menu(
                        floatingActionButton = {
                            FloatingActionButton(
                                icon = { IconSave() },
                                onClick = { navigateTo(Screen.TransactionEditScreen()) }
                            )
                        },
                        bodyContent = { TransactionEditScreen(screen) },
                        navigateTo = navigateTo
                    )
            }
        }
    }
}
