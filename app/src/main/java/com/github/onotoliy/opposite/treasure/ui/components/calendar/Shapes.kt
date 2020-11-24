package com.github.onotoliy.opposite.treasure.ui.components.calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun Circle(color: Color) {
    Canvas(Modifier.fillMaxSize()) {
        drawCircle(color)
    }
}

@Composable
fun SemiRect(color: Color, lookingLeft: Boolean = true) {
    Canvas(Modifier.fillMaxSize()) {
        val offset = if (lookingLeft) {
            Offset(0f, 0f)
        } else {
            Offset(size.width / 2, 0f)
        }
        val size = Size(width = size.width / 2, height = size.height)

        drawRect(size = size, topLeft = offset, color = color)
    }
}