package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.material.LinearProgressIndicator
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.observe
import com.github.onotoliy.opposite.treasure.ui.screens.views.TransactionView

@Composable
fun TransactionScreen(model: Screen.TransactionScreen, navigateTo: (Screen) -> Unit) {
    model.pending.observe()?.let { pending ->
        Column {
            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            model.transaction.observe()?.let {
                TransactionView(data = it, navigateTo = navigateTo)
            }
        }
    }
}