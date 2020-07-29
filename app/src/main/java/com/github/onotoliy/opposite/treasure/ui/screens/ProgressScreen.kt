package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.Arrangement
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview

@Preview
@Composable
fun ProgressScreen() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalGravity = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text(text = "Выполняется загрузка данных.", textAlign = TextAlign.Center)
        Text(text = "Пожалуйста, подождите...", textAlign = TextAlign.Center)
    }
}