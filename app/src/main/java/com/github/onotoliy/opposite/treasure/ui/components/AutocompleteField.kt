package com.github.onotoliy.opposite.treasure.ui.components

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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.core.HasName
import com.github.onotoliy.opposite.data.core.HasUUID
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import java.util.*
import kotlin.concurrent.schedule

@Composable
fun <T> AutocompleteField(
    label: String,
    value: OptionVO = OptionVO(),
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyleLeft,
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (OptionVO) -> Unit,
    onSearchValue: (String?) -> LiveData<List<T>>
) where T : HasUUID, T : HasName {
    val state = remember { mutableStateOf(listOf<OptionVO>()) }
    val timer = remember { mutableStateOf("") }
    val selected = remember { mutableStateOf(value) }
    val expanded = remember { mutableStateOf(false) }
    val reload: MutableLiveData<Boolean> = MutableLiveData(false)

    reload.observeForever { beep ->
        if (beep) {
            onSearchValue(timer.value).observeForever { list ->
                state.value = list.map { OptionVO(it.uuid, it.name) }
                timer.value = ""
                expanded.value = true
                reload.postValue(false)
            }
        }
    }

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
                    selected.value = OptionVO("", it)
                    expanded.value = false

                    Timer().schedule(2000) {
                        if (timer.value.isNotEmpty()) {
                            reload.postValue(true)
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

@Composable
fun AutocompleteField(
    label: String,
    list: List<OptionVO>,
    search: (String) -> List<OptionVO>,
    value: OptionVO = OptionVO(),
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyleLeft,
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (OptionVO) -> Unit
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
                    selected.value = OptionVO("", it)
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
