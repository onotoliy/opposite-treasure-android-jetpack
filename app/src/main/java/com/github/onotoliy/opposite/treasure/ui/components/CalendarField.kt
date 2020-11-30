package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.currentTextStyle
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.ui.tooling.preview.Preview
import com.github.onotoliy.opposite.treasure.ui.H6_BOLD
import com.github.onotoliy.opposite.treasure.ui.IconLeftArrow
import com.github.onotoliy.opposite.treasure.ui.IconRightArrow
import com.github.onotoliy.opposite.treasure.ui.components.DayOfWeek.*
import com.github.onotoliy.opposite.treasure.ui.components.calendar.CELL_SIZE

data class Month(
    val code: String,
    val name: String,
    val year: String,
    val weeks: List<Week>
)

enum class DayOfWeek(val label: String, val color: Color) {
    Monday("Пн", Color.Black),
    Tuesday("Вт", Color.Black),
    Wednesday("Ср", Color.Black),
    Thursday("Чт", Color.Black),
    Friday("Пт", Color.Black),
    Saturday("Сб", Color.Red),
    Sunday("Вc", Color.Red)
}

data class Week(
    val dates: List<Day> = listOf()
)

data class Day(
    val dayOfWeek: DayOfWeek,
    val date: String
)

val Bold = TextStyle(
    fontWeight = FontWeight.Bold
)

@Preview
@Composable
fun CalendarFieldPreview() {
    val month = Month(
        code = "06",
        name = "Июнь",
        year = "2020",
        weeks = listOf(
            Week(
                listOf(
                    Day(Monday, ""),
                    Day(Tuesday, ""),
                    Day(Wednesday, ""),
                    Day(Thursday, "1"),
                    Day(Friday, "2"),
                    Day(Saturday, "3"),
                    Day(Sunday, "4")
                )
            ),
            Week(
                listOf(
                    Day(Monday, "5"),
                    Day(Tuesday, "6"),
                    Day(Wednesday, "7"),
                    Day(Thursday, "8"),
                    Day(Friday, "9"),
                    Day(Saturday, "10"),
                    Day(Sunday, "11")
                )
            ),
            Week(
                listOf(
                    Day(Monday, "12"),
                    Day(Tuesday, "13"),
                    Day(Wednesday, "14"),
                    Day(Thursday, "15"),
                    Day(Friday, "16"),
                    Day(Saturday, "17"),
                    Day(Sunday, "18")
                )
            ),
            Week(
                listOf(
                    Day(Monday, "19"),
                    Day(Tuesday, "20"),
                    Day(Wednesday, "21"),
                    Day(Thursday, "22"),
                    Day(Friday, "23"),
                    Day(Saturday, "24"),
                    Day(Sunday, "25")
                )
            ),
            Week(
                listOf(
                    Day(Monday, "26"),
                    Day(Tuesday, "27"),
                    Day(Wednesday, "28"),
                    Day(Thursday, "29"),
                    Day(Friday, "30"),
                    Day(Saturday, ""),
                    Day(Sunday, "")
                )
            )
        )
    )
    CalendarPopup(month = month)
}



@Composable
fun CalendarPopup(month: Month) {
    Popup {
        Column {
            Row {
                Row(
                    modifier = Modifier
                        .preferredSize(width = CELL_SIZE, height = CELL_SIZE / 2)
                        .padding(0.dp, 0.dp, CELL_SIZE / 2, 0.dp)
                ) {
                    IconLeftArrow()
                }
                Text(
                    modifier = Modifier
                        .preferredSize(width = CELL_SIZE + CELL_SIZE + CELL_SIZE+ CELL_SIZE, height = CELL_SIZE / 2),
                    text = month.name,
                    style = H6_BOLD,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier
                        .preferredSize(width = CELL_SIZE, height = CELL_SIZE / 2),
                    text = month.year,
                    style = H6_BOLD,
                    color = Color.Black
                )
                Row(
                    modifier = Modifier
                        .preferredSize(width = CELL_SIZE, height = CELL_SIZE / 2)
                        .padding(CELL_SIZE / 2, 0.dp, 0.dp, 0.dp)
                ) {
                    IconRightArrow()
                }
            }

            Row {
                for (day in DayOfWeek.values()) {
                    Day(name = day.label, style = Bold, color = day.color)
                }
            }

            month.weeks.forEach { week ->
                Row {
                    week.dates.forEach { day ->
                        Day(
                            name = day.date,
                            style = currentTextStyle(),
                            color = day.dayOfWeek.color
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Day(name: String, style: TextStyle, color: Color, onClick: () -> Unit = { }) {
    Surface(
        modifier = Modifier
            .preferredSize(width = CELL_SIZE, height = CELL_SIZE)
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Text(
            modifier = Modifier.wrapContentSize(Alignment.Center),
            text = name,
            style = style,
            color = color
        )
    }
}

