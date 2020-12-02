package com.github.onotoliy.opposite.treasure.ui.components

import android.util.Log
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.treasure.ui.IconAccountCircle
import java.util.*
import kotlin.concurrent.schedule

@Preview
@Composable
fun AutocompleteFieldPreview() {
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

    AutocompleteField(
        label = "Пользователь",
        list = list,
        leadingIcon = {
            IconAccountCircle()
        },
        search = { q ->
            list.filter {
                it.name.toLowerCase(Locale.getDefault())
                    .contains(q.toLowerCase(Locale.getDefault()))
            }
        },
        onValueChange = { }
    )
}

@Composable
fun AutocompleteField(
    label: String,
    list: List<Option>,
    search: (String) -> List<Option>,
    value: Option = Option(),
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyleLeft,
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (Option) -> Unit
) {
    val state = remember { mutableStateOf(list) }
    val timer = remember { mutableStateOf("") }
    val selected = remember { mutableStateOf(value) }
    val expanded = remember { mutableStateOf(false) }

    DropdownMenu(
        toggle = {
            TextField(
                label = label,
                modifier = modifier,
                textStyle = textStyle,
                leadingIcon = leadingIcon,
                value = selected.value.name,
                onValueChange = {
                    timer.value = it
                    selected.value = Option("", it)
                    expanded.value = false

                    Timer().schedule(2000) {
                        if (timer.value.isNotEmpty()) {
                            state.value = search(timer.value)
                            timer.value = ""
                            expanded.value = true
                        }
                    }
                }
            )
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
