package com.github.onotoliy.opposite.treasure.ui.screens

import android.accounts.AccountManager
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.animation.Crossfade
import androidx.ui.material.Surface
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.auth.*
import com.github.onotoliy.opposite.treasure.models.TreasureScreenModel
import com.github.onotoliy.opposite.treasure.ui.Menu

@Composable
fun TreasureScreen(firstScreen: Screen, manager: AccountManager) {
    val state = state { firstScreen }
    val navigateTo: (Screen) -> Unit = {
        state.value = it
    }
    val model = TreasureScreenModel(manager)

    Crossfade(state.value) { screen ->
        Surface {
            when (screen) {
                is Screen.LoginScreen -> LoginScreen { account, password, token ->
                    manager.addAccount(account, password, token)
                    navigateTo(Screen.HomeScreen)
                }
                is Screen.HomeScreen ->
                    Menu(
                        bodyContent = {
                            DepositScreen(model.getDepositModel(manager.getUUID()))
                        },
                        navigateTo = navigateTo
                    )
                is Screen.DepositScreen ->
                    Menu(
                        bodyContent = {
                            DepositScreen(model.getDepositModel(screen.deposit))
                        },
                        navigateTo = navigateTo
                    )
                is Screen.EventScreen ->
                    Menu(
                        bodyContent = {
                            EventScreen(model.getEventModel(screen.event), navigateTo = navigateTo)
                        },
                        navigateTo = navigateTo
                    )
                is Screen.TransactionScreen ->
                    Menu(
                        bodyContent = { TransactionScreen(model.getTransactionModel(screen.transaction), navigateTo = navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.DepositPageScreen ->
                    Menu(
                        bodyContent = { DepositPageScreen(model.getDepositPageModel(), navigateTo) },
                        navigateTo = navigateTo
                    )
                is Screen.EventPageScreen ->
                    Menu(
                        bodyContent = {
                            EventPageScreen(model.getEventPageModel(screen.offset, screen.numberOfRows, screen.default ?: listOf()), navigateTo)
                        },
                        navigateTo = navigateTo
                    )
                is Screen.TransactionPageScreen ->
                    Menu(
                        bodyContent = {
                            TransactionPageScreen(model.getTransactionPageModel(screen.offset, screen.numberOfRows, screen.default ?: listOf()), navigateTo)
                        },
                        navigateTo = navigateTo
                    )
            }
        }
    }
}
