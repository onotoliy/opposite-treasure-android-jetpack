package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val TextStyleCenter = TextStyle(
    fontSize = TextUnit.Em(5),
    textAlign = TextAlign.Center,
    color = Color.Black
)

val TextStyleLeft = TextStyle(
    fontSize = TextUnit.Em(5),
    textAlign = TextAlign.Left,
    color = Color.Black
)

@Composable
fun TextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    editable: Boolean = true,
    labelColor: Color = MaterialTheme.colors.primary,
    labelFontSize: Int = 16,
    valueColor: Color = Color.Black,
    valueFontSize: Int = 20,
    background: Color = Color.White,
    leadingIcon: @Composable() (() -> Unit)? = null,
    textAlign: TextAlign = TextAlign.Left,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    if (editable) {
        androidx.compose.material.TextField(
            label = {
                androidx.compose.material.Text(
                    text = label,
                    color = labelColor,
                    fontSize = if (value.isNotBlank()) labelFontSize.sp else valueFontSize.sp,
                )
            },
            modifier = modifier,
            value = value,
            leadingIcon = leadingIcon,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = valueColor, fontSize = valueFontSize.sp, textAlign = textAlign),
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
            textAlign = textAlign
        )
    }
}
