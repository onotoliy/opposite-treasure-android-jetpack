package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.Popup
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.Row
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.*
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.observe

@Preview
@Composable
fun TransactionEditScreenPreview() {
    Popup {
        RadioGroup(options = listOf("1", "2", "3", "4"), selectedOption = "1", onSelectedChange = {})
    }
}

@Composable
fun TransactionEditScreen(model: Screen.TransactionEditScreen) {
    model.pending.observe()?.let { pending ->
        Column(modifier = Modifier.padding(5.dp)) {
            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            TransactionEditScreenPreview()
        }
    }
}