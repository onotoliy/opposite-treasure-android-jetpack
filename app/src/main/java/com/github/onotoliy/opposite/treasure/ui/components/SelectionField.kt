package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.github.onotoliy.opposite.data.Option

@Preview
@Composable
fun SelectionFieldPreview() {
    val list = listOf(
        Option("1", "Иван"),
        Option("2", "Петр"),
        Option("3", "Алексей"),
        Option("4", "Владимир"),
        Option("5", "Игорь"),
        Option("6", "Валентин"),
        Option("7", "Анатолий"),
        Option("8", "Евгений"),
        Option("9", "Александр"),
        Option("10", "Олег")
    )

    SelectionField(
        label = "Пользователь",
        list = list,
        modifier = Modifier.fillMaxWidth(0.8f),
        onValueChange = { }
    )
}

@Composable
fun SelectionField(
    label: String,
    list: List<Option>,
    value: Option = Option(),
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyleLeft,
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (Option) -> Unit
) {
    val state = remember { mutableStateOf(list) }
    val selected = remember { mutableStateOf(value) }
    val expanded = remember { mutableStateOf(false) }

    DropdownMenu(
        toggle = {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = modifier
                    .clickable(onClick = { expanded.value = true })
                    .drawBehind {
                        val width = size.width
                        val height = size.height

                        drawLine(
                            color = Color.Black,
                            start = Offset(0.0f, height),
                            end = Offset(width, height),
                            strokeWidth = Stroke.DefaultMiter
                        )
                    }
            ) {
                if (selected.value.uuid.isEmpty()) {
                    Text(
                        modifier = Modifier.padding(10.dp, 0.dp),
                        text = label,
                        fontSize = TextUnit.Em(4),
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colors.primary
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(10.dp, 0.dp),
                        text = label,
                        fontSize = TextUnit.Em(3),
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colors.primary
                    )
                }
                Text(
                    modifier = Modifier.padding(10.dp, 0.dp),
                    text = selected.value.name,
                    fontSize = TextUnit.Em(4),
                    textAlign = TextAlign.Left,
                    color = Color.Black
                )
            }
        },
        dropdownModifier = Modifier.fillMaxWidth(1.0f).padding(0.dp),
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        state.value.forEach {
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(1.0f).padding(0.dp),
                onClick = {
                    selected.value = it
                    expanded.value = false
                    onValueChange(it)
                }
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(1.0f).padding(0.dp),
                    text = it.name
                )
            }
        }
    }
}