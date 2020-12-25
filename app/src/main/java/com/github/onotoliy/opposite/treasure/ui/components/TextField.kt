package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AmbientTextStyle
import androidx.compose.material.MaterialTheme
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
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = AmbientTextStyle.current,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    isErrorValue: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    onImeActionPerformed: (ImeAction, SoftwareKeyboardController?) -> Unit = { _, _ -> },
    onTextInputStarted: (SoftwareKeyboardController) -> Unit = {}
) {
        androidx.compose.material.TextField(
            label = {
                androidx.compose.material.Text(
                    text = label,
                    color = MaterialTheme.colors.primary,
                    fontSize = textStyle.fontSize,
                )
            },
            modifier = modifier,
            textStyle = textStyle,
            value = value,
            isErrorValue = isErrorValue,
            leadingIcon = leadingIcon,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            backgroundColor = Color.White,
            visualTransformation = visualTransformation,
            onImeActionPerformed = onImeActionPerformed,
            onTextInputStarted = onTextInputStarted
        )
}
