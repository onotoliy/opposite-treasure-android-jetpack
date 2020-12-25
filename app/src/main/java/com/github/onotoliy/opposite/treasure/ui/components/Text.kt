package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun Text(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyleCenter,
    background: Color = Color.White,
    enabled: Boolean = false,
    onClick: () -> Unit = { }
) {
    Column(
        modifier = modifier
            .background(background, shape = RoundedCornerShape(topLeft = 4.dp, topRight = 4.dp))
            .padding(bottom = 10.dp)
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        androidx.compose.material.Text(
            modifier = if (value.isNotEmpty()) {
                Modifier.padding(horizontal = 20.dp)
            } else {
                Modifier.padding(horizontal = 20.dp, vertical = 15.dp)
            },
            text = label,
            fontSize = if (value.isEmpty()) {
                textStyle.fontSize
            } else{
                textStyle.fontSize - TextUnit.Em(1)
            },
            color = MaterialTheme.colors.primary
        )

        if (value.isNotEmpty()) {
            androidx.compose.material.Text(
                modifier = Modifier.fillMaxWidth(),
                text = value,
                fontSize = textStyle.fontSize,
                textAlign = textStyle.textAlign,
                color = textStyle.color
            )
        }
    }
}
