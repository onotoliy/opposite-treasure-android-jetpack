package com.github.onotoliy.opposite.treasure.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun TreasureTheme(content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = lightColors(),
        typography = typography,
        shapes = shapes,
        content = content
    )
}
