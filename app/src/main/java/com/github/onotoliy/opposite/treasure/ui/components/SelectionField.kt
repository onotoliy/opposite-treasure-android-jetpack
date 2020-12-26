package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO

@Composable
fun SelectionField(
    label: String,
    list: List<OptionVO>,
    modifier: Modifier = Modifier,
    value: OptionVO = OptionVO(),
    onValueChange: (OptionVO) -> Unit
) {
    val selected = remember { mutableStateOf(value) }
    val expanded = remember { mutableStateOf(false) }

    DropdownMenu(
        toggle = {
            LabeledText(
                label = label,
                value = value.name,
                modifier = modifier,
                onClick = { expanded.value = true }
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
                    selected.value = it
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
