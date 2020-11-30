package com.github.onotoliy.opposite.treasure.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.ui.tooling.preview.Preview
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.ui.activity.EventTab
import com.github.onotoliy.opposite.treasure.ui.components.TextField
import com.github.onotoliy.opposite.treasure.ui.components.TextStyleLeft
import com.github.onotoliy.opposite.treasure.ui.components.calendar.CalendarScreenP

@Preview
@Composable
fun EventEditView() {
    val name = remember { mutableStateOf("") }
    val contribution = remember { mutableStateOf("0.0") }
    val total = remember { mutableStateOf("0.0") }
    Column {
        TextField(
            value = name.value,
            label = stringResource(id = R.string.event_edit_name),
            onValueChange = { name.value = it },
            textStyle = TextStyleLeft
        )
        TextField(
            value = contribution.value,
            label = stringResource(id = R.string.event_edit_contribution),
            onValueChange = { contribution.value = it },
            keyboardType = KeyboardType.Number,
            textStyle = TextStyleLeft
        )
        TextField(
            value = total.value,
            label = stringResource(id = R.string.event_edit_total),
            onValueChange = { total.value = it },
            keyboardType = KeyboardType.Number,
            textStyle = TextStyleLeft
        )
    }
}