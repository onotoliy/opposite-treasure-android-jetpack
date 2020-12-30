package com.github.onotoliy.opposite.treasure.ui.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AmbientTextStyle
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.ui.H6_BOLD
import com.github.onotoliy.opposite.treasure.ui.IconLeftArrow
import com.github.onotoliy.opposite.treasure.ui.IconRightArrow
import com.github.onotoliy.opposite.treasure.ui.components.LabeledText
import com.github.onotoliy.opposite.treasure.utils.fromShortDate
import com.github.onotoliy.opposite.treasure.utils.toISO

@Composable
fun CalendarField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    editable: Boolean = true,
    labelColor: Color = MaterialTheme.colors.primary,
    labelFontSize: Int = 4,
    valueColor: Color = Color.Black,
    valueFontSize: Int = 5,
    background: Color = Color.White,
    textAlign: TextAlign = TextAlign.Left,
    leadingIcon: @Composable() (() -> Unit)? = null,
    divider: @Composable() (() -> Unit)? = null
) {
    val model = remember { mutableStateOf(CalendarModel(value), neverEqualPolicy()) }
    val selected = remember { mutableStateOf(value) }
    val expanded = remember { mutableStateOf(false) }

    LabeledText(
        modifier = modifier.drawBehind {
            drawLine(
                color = Color.Black,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 4f,
                alpha = 0.54f
            )
        },
        label = label,
        value = selected.value,
        labelColor = labelColor,
        labelFontSize = labelFontSize,
        valueColor = valueColor,
        valueFontSize = valueFontSize,
        background = background,
        textAlign = textAlign,
        leadingIcon = leadingIcon,
        onClick = if (editable) { { expanded.value = true } } else null
    )

    if (expanded.value) {
        Dialog(onDismissRequest = { expanded.value = false }) {
            Column {
                CalendarHeader(model = model.value)
                CalendarWeeks(model = model.value, set = { selected.value = it})
                CalendarActions(model = model, selected = selected, expanded = expanded, onValueChange = onValueChange)
            }
        }
    }
}

@Composable
private fun CalendarHeader(model: CalendarModel) {
    val year = remember { mutableStateOf(false) }
    val month = remember { mutableStateOf(false) }

    Surface {
        Row {
            Box(modifier = Modifier.preferredSize(48.dp)) {
                IconLeftArrow(
                    Modifier
                        .preferredSize(12.dp)
                        .align(Alignment.Center)
                        .clickable(onClick = model::previousMonth)
                )
            }
            DropdownMenu(
                toggle = {
                    Box(
                        modifier = Modifier
                            .clickable { month.value = true }
                            .preferredSize(height = 48.dp, width = 144.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = model.headerMonth.label,
                            style = H6_BOLD,
                            color = Color.Black
                        )
                    }
                },
                expanded = month.value,
                onDismissRequest = { month.value = false }
            ) {
                Month.values().forEach {
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(1.0f).padding(0.dp),
                        onClick = {
                            month.value = false
                            model.onSelectedMonth(it)
                        }
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(1.0f).padding(0.dp),
                            text = it.label
                        )
                    }
                }
            }
            DropdownMenu(
                toggle = {
                    Box(
                        modifier = Modifier
                            .clickable { year.value = true }
                            .preferredSize(height = 48.dp, width = 96.dp)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = model.headerYear,
                            style = H6_BOLD,
                            color = Color.Black
                        )
                    }
                },
                expanded = year.value,
                onDismissRequest = { year.value = false }
            ) {
                model.years.forEach {
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(1.0f).padding(0.dp),
                        onClick = {
                            year.value = false
                            model.onSelectedYear(it)
                        }
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(1.0f).padding(0.dp),
                            text = it
                        )
                    }
                }
            }
            Box(modifier = Modifier.preferredSize(48.dp)) {
                IconRightArrow(
                    Modifier
                        .preferredSize(12.dp)
                        .align(Alignment.Center)
                        .clickable(onClick = model::nextMonth)
                )
            }
        }
    }
}

@Composable
private fun CalendarWeeks(model: CalendarModel, set: (String) -> Unit) {
    Column {
        Row {
            for (day in DayOfWeek.values()) {
                CalendarDay(
                    name = day.label,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    color = day.color
                )
            }
        }
        model.weeks.forEach { week ->
            Row {
                DayOfWeek.values().forEach { dayOfWeek ->
                    val day = week.dates.firstOrNull { it.dayOfWeek == dayOfWeek }

                    CalendarDay(
                        name = day?.date ?: "",
                        color = day?.dayOfWeek?.color ?: Color.Black,
                        isSelected = day?.selected ?: false,
                        enabled = day != null,
                        onClick = { day?.let { model.onSelectedDay(it, set) } }
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    name: String,
    style: TextStyle = AmbientTextStyle.current,
    color: Color = Color.Black,
    isSelected: Boolean = false,
    enabled: Boolean = false,
    onClick: () -> Unit = { }
) {
    Surface(
        modifier = Modifier.preferredSize(48.dp).clickable(enabled = enabled, onClick = onClick),
    ) {
        val modifier = if (isSelected) {
            Modifier.clip(CircleShape).background(MaterialTheme.colors.primary, CircleShape)
        } else {
            Modifier
        }

        Text(
            modifier = modifier.wrapContentSize(Alignment.Center),
            text = name,
            style = style,
            color = if (isSelected) Color.White else color
        )
    }
}

@Composable
private fun CalendarActions(
    model: MutableState<CalendarModel>,
    selected: MutableState<String>,
    expanded: MutableState<Boolean>,
    onValueChange: (String) -> Unit
) {
    Surface {
        Row {
            Box(modifier = Modifier.preferredSize(height = 48.dp, width = 192.dp)) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = selected.value
                )
            }
            Box(modifier = Modifier.preferredSize(height = 48.dp, width = 144.dp)) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        expanded.value = false
                        onValueChange(model.value.selected.fromShortDate().toISO())
                    }
                ) {
                    Text(text = stringResource(R.string.choose))
                }

            }
        }
    }
}
