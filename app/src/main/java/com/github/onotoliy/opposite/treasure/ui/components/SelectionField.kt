package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO

@Composable
fun SelectionField(
    label: String,
    value: OptionVO,
    list: List<OptionVO>,
    onValueChange: (OptionVO) -> Unit,
    modifier: Modifier = Modifier,
    editable: Boolean = true,
    labelColor: Color = MaterialTheme.colors.primary,
    labelFontSize: Int = 4,
    valueColor: Color = Color.Black,
    valueFontSize: Int = 5,
    background: Color = Color.White,
    textAlign: TextAlign = TextAlign.Left,
    leadingIcon: @Composable() (() -> Unit)? = null
) {
    val expanded = remember { mutableStateOf(false) }

    DropdownMenu(
        toggle = {
            LabeledText(
                label = label,
                value = value.name,
                modifier = modifier.drawBehind {
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 4f,
                        alpha = 0.54f
                    )
                },
                onClick = if (editable) { { expanded.value = true } } else null,
                textAlign = textAlign,
                leadingIcon = leadingIcon,
                background = background,
                valueFontSize = valueFontSize,
                valueColor = valueColor,
                labelFontSize = labelFontSize,
                labelColor = labelColor
            )
        },
        dropdownModifier = Modifier.fillMaxWidth(1.0f).padding(0.dp),
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        list.forEach {
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(1.0f).padding(0.dp),
                onClick = {
                    expanded.value = false
                    onValueChange(it)
                }
            ) {
                androidx.compose.material.Text(
                    modifier = Modifier.fillMaxWidth(1.0f).padding(0.dp),
                    text = it.name
                )
            }
        }
    }
}
