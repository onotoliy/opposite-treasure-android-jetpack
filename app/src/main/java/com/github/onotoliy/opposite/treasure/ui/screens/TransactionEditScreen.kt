package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.LinearProgressIndicator
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.observe

@Composable
fun TransactionEditScreen(model: Screen.TransactionEditScreen) {
    model.pending.observe()?.let { pending ->
        Column(modifier = Modifier.padding(5.dp)) {
            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

        }
    }
}