package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.foundation.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

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
    value: String = "",
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyleCenter,
    label: String = "",
    disable: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    isErrorValue: Boolean = false,
    imeAction: ImeAction = ImeAction.Unspecified,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    onImeActionPerformed: (ImeAction, SoftwareKeyboardController?) -> Unit = { _, _ -> },
    onTextInputStarted: (SoftwareKeyboardController) -> Unit = {},
    onValueChange: (String) -> Unit
) {
    if (disable) {
        Text(
            label = label,
            value = value,
            modifier = modifier,
            textStyle = textStyle
        )
    } else {
        TextField(
            label = {
                Text(
                    text = label,
                    color = MaterialTheme.colors.primary,
                    fontSize = if (value.isEmpty()) textStyle.fontSize else textStyle.fontSize - TextUnit.Em(1)
                )
            },
            modifier = modifier,
            textStyle = textStyle,
            value = value,
            isErrorValue = isErrorValue,
            leadingIcon = leadingIcon,
            onValueChange = onValueChange,
            keyboardType = keyboardType,
            backgroundColor = Color.White,
            imeAction = imeAction,
            visualTransformation = visualTransformation,
            onImeActionPerformed = onImeActionPerformed,
            onTextInputStarted = onTextInputStarted
        )
    }
}
