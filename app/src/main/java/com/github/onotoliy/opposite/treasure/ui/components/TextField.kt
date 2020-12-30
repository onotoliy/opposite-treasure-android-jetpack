package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em

@Composable
fun TextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    editable: Boolean = true,
    labelColor: Color = MaterialTheme.colors.primary,
    labelFontSize: Int = 4,
    valueColor: Color = Color.Black,
    valueFontSize: Int = 5,
    background: Color = Color.White,
    leadingIcon: @Composable() (() -> Unit)? = null,
    textAlign: TextAlign = TextAlign.Left,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    if (editable) {
        androidx.compose.material.TextField(
            label = {
                Text(
                    text = label,
                    color = labelColor,
                    fontSize = if (value.isNotBlank()) labelFontSize.em else valueFontSize.em,
                )
            },
            modifier = modifier,
            value = value,
            leadingIcon = leadingIcon,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = valueColor, fontSize = valueFontSize.em, textAlign = textAlign),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            backgroundColor = background,
            visualTransformation = visualTransformation,
        )
    } else {
        LabeledText(
            label = label,
            value = value,
            modifier = modifier.drawBehind {
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 4f,
                    alpha = 0.54f
                )
            },
            labelColor = labelColor,
            labelFontSize = labelFontSize,
            valueColor = valueColor,
            valueFontSize = valueFontSize,
            background = background,
            leadingIcon = leadingIcon,
            textAlign = textAlign,
            divider = null
        )
    }
}
