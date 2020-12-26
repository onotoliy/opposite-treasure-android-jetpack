package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import java.util.*
import kotlin.concurrent.schedule

@Composable
fun AutocompleteField(
    label: String,
    modifier: Modifier = Modifier,
    value: OptionVO = OptionVO(),
    list: List<OptionVO> = listOf(),
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (OptionVO) -> Unit,
    onSearchValue: (String?) -> Unit
) {
    val timer = remember { mutableStateOf("") }
    val selected = remember { mutableStateOf(value) }
    val expanded = remember { mutableStateOf(false) }

    DropdownMenu(
        toggle = {
            TextField(
                label = label,
                modifier = modifier,
                leadingIcon = leadingIcon,
                value = selected.value.name,
                onValueChange = {
                    timer.value = it
                    selected.value = OptionVO("", it)
                    expanded.value = false

                    Timer().schedule(2000) {
                        if (timer.value.isNotEmpty()) {
                            onSearchValue(timer.value)
                            timer.value = ""
                            expanded.value = true
                        }
                    }
                }
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
                    selected.value = it
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
