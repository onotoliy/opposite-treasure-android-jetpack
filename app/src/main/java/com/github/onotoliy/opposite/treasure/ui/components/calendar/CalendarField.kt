package com.github.onotoliy.opposite.treasure.ui.components.calendar

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.currentTextStyle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.onotoliy.opposite.treasure.ui.H6_BOLD
import com.github.onotoliy.opposite.treasure.ui.IconLeftArrow
import com.github.onotoliy.opposite.treasure.ui.IconRightArrow
import com.github.onotoliy.opposite.treasure.utils.observe

@Composable
private fun Modifier.selected(isSelected: Boolean): Modifier =
    if (isSelected) this.clip(CircleShape).background(MaterialTheme.colors.primary, CircleShape) else this

@Composable
fun CalendarField(
    label: String,
    value: String = "",
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    val model = remember { mutableStateOf(CalendarModel()) }
    val selected = remember { mutableStateOf(value) }
    val expanded = remember { mutableStateOf(false) }

    model.value.selected.observeForever {
        selected.value = it
        onValueChange(it)
    }

    Surface(modifier = modifier.clip(RoundedCornerShape(topLeft = 4.dp, topRight = 4.dp))) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .height(60.dp)
                .padding(vertical = 3.dp)
                .clickable(onClick = { expanded.value = true })
                .drawBehind {
                    val width = size.width
                    val height = size.height

                    drawLine(
                        color = Color.Gray,
                        start = Offset(0.0f, height),
                        end = Offset(width, height),
                        strokeWidth = Stroke.DefaultMiter
                    )
                }
        ) {
            if (selected.value.isEmpty()) {
                Text(
                    modifier = Modifier.padding(16.dp, 0.dp),
                    text = label,
                    fontSize = TextUnit.Em(4),
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colors.primary
                )
            } else {
                Text(
                    modifier = Modifier.padding(16.dp, 0.dp),
                    text = label,
                    fontSize = TextUnit.Em(3),
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colors.primary
                )
            }
            Text(
                modifier = Modifier.padding(16.dp, 0.dp),
                text = selected.value,
                fontSize = TextUnit.Em(4),
                textAlign = TextAlign.Left,
                color = Color.Black
            )
        }
    }
    if (expanded.value) {
        Dialog(onDismissRequest = {}) {
            Column {
                CalendarHeader(model = model.value)
                CalendarWeeks(model = model.value)
                CalendarActions(model = model.value, expanded = expanded)
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
                            text = model.headerMonth.observe() ?: "",
                            style = H6_BOLD,
                            color = Color.Black
                        )
                    }
                },
                expanded = month.value,
                onDismissRequest = { month.value = false }
            ) {
                model.months.forEach {
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(1.0f).padding(0.dp),
                        onClick = {
                            month.value = false
                            model.onSelectedMonth(it)
                        }
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(1.0f).padding(0.dp),
                            text = it
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
                            text = model.headerYear.observe() ?: "",
                            style = H6_BOLD,
                            color = Color.Black
                        )
                    }
                },
                expanded = year.value,
                onDismissRequest = { year.value = false }
            ) {
                model.years.observe()?.forEach {
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
private fun CalendarWeeks(model: CalendarModel) {
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
        model.weeks.observe()?.forEach { week ->
            Row {
                DayOfWeek.values().forEach { dayOfWeek ->
                    val day = week.dates.firstOrNull { it.dayOfWeek == dayOfWeek }

                    CalendarDay(
                        name = day?.date ?: "",
                        color = day?.dayOfWeek?.color ?: Color.Black,
                        isSelected = day?.selected ?: false,
                        enabled = day != null,
                        onClick = { day?.let { model.onSelectedDay(it) } }
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    name: String,
    style: TextStyle = currentTextStyle(),
    color: Color = Color.Black,
    isSelected: Boolean = false,
    enabled: Boolean = false,
    onClick: () -> Unit = { }
) {
    Surface(
        modifier = Modifier.preferredSize(48.dp).clickable(enabled = enabled, onClick = onClick),
    ) {
        Text(
            modifier = Modifier.selected(isSelected).wrapContentSize(Alignment.Center),
            text = name,
            style = style,
            color = if (isSelected) Color.White else color
        )
    }
}

@Composable
private fun CalendarActions(model: CalendarModel, expanded: MutableState<Boolean>) {
    Surface {
        Row {
            Box(modifier = Modifier.preferredSize(height = 48.dp, width = 192.dp)) {
                model.selected.observe()?.let {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = it
                    )
                }
            }
            Box(modifier = Modifier.preferredSize(height = 48.dp, width = 144.dp)) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = { expanded.value = false }
                ) {
                    Text(text = "Выбрать")
                }

            }
        }
    }
}
