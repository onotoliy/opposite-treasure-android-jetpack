package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import java.util.*
import kotlin.concurrent.schedule

@Composable
fun AutocompleteField(
    label: String,
    value: OptionVO,
    list: List<OptionVO>,
    onSearchValue: (String?) -> Unit,
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
            TextField(
                label = label,
                modifier = modifier,
                leadingIcon = leadingIcon,
                value = value.name,
                onValueChange = {
                    onValueChange(OptionVO("", it))
                    expanded.value = false

                    Timer().schedule(2000) {
                        if (value.name.isNotEmpty()) {
                            onSearchValue(value.name)
                            expanded.value = true
                        }
                    }
                },
                editable = editable,
                labelColor = labelColor,
                labelFontSize = labelFontSize,
                valueColor = valueColor,
                valueFontSize = valueFontSize,
                background = background,
                textAlign = textAlign
            )
        },
        dropdownModifier = Modifier.fillMaxWidth(1.0f),
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        list.forEach {
            DropdownMenuItem(
                modifier = Modifier.fillMaxWidth(1.0f),
                onClick = {
                    expanded.value = false
                    onValueChange(it)
                }
            ) {
                androidx.compose.material.Text(
                    modifier = Modifier.fillMaxWidth(1.0f),
                    text = it.name
                )
            }
        }
    }
}
