package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.observe
import com.github.onotoliy.opposite.treasure.ui.screens.views.TransactionView

@Composable
fun TransactionScreen(model: Screen.TransactionScreen, navigateTo: (Screen) -> Unit) {/*
    model.pending.observe()?.let { pending ->
        Column {
            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            model.transaction.observe()?.let {
                TransactionView(data = it, navigateTo = navigateTo)
            }
        }
    }*/
}