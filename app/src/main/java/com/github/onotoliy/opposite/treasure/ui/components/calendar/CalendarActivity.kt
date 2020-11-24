package com.github.onotoliy.opposite.treasure.ui.components.calendar


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.github.onotoliy.opposite.treasure.R

fun launchCalendarActivity(context: Context) {
    val intent = Intent(context, CalendarActivity::class.java)
    context.startActivity(intent)
}

class CalendarActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(color = MaterialTheme.colors.primary) {
                Surface {
                    CalendarScreen(onBackPressed = { finish() })
                }
            }
        }
    }
}

@Preview
@Composable
fun CalendarScreenP() {
    Surface(color = MaterialTheme.colors.primary) {
        Surface {
            CalendarScreen(onBackPressed = { })
        }
    }
}

@Composable
fun CalendarScreen(onBackPressed: () -> Unit) {
    val calendarViewModel: CalendarViewModel = viewModel()
    val calendarYear = calendarViewModel.calendarYear

    CalendarContent(
        selectedDates = calendarViewModel.datesSelected.toString(),
        calendarYear = calendarYear,
        onDayClicked = { calendarDay, calendarMonth ->
            calendarViewModel.onDaySelected(
                DaySelected(calendarDay.value.toInt(), calendarMonth, calendarYear)
            )
        },
        onBackPressed = onBackPressed
    )
}

@Composable
private fun CalendarContent(
    selectedDates: String,
    calendarYear: CalendarYear,
    onDayClicked: (CalendarDay, CalendarMonth) -> Unit,
    onBackPressed: () -> Unit
) {
    Surface(color = MaterialTheme.colors.primary) {
        Column {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedDates.isEmpty()) "Select Dates"
                        else selectedDates
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Image(asset = vectorResource(id = R.drawable.ic_trending_down))
                    }
                },
                backgroundColor = MaterialTheme.colors.primaryVariant
            )
            Surface {
                Calendar(calendarYear, onDayClicked)
            }
        }
    }
}