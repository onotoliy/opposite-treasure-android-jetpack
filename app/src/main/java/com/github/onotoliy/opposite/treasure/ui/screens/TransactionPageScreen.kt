package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.observe
import com.github.onotoliy.opposite.treasure.ui.screens.views.TransactionPageView

@Composable
fun TransactionPageScreen(
    model: Screen.TransactionPageScreen,
    navigateTo: (Screen) -> Unit
) {/*
    model.pending.observe()?.let { pending ->
        Column {
            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            model.page.observe()?.let { view ->
                TransactionPageView(
                    view = view,
                    navigateTo = navigateTo,
                    navigateToNextPageScreen = { offset, numberOfRows, default ->
                        navigateTo(
                            Screen.TransactionPageScreen(
                                view.copy(
                                    offset = offset,
                                    numberOfRows = numberOfRows,
                                    default = default
                                )
                            )
                        )
                    }
                )
            }
        }
    }*/
}
