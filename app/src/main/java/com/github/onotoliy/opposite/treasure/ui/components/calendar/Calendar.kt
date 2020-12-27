package com.github.onotoliy.opposite.treasure.ui.components.calendar

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.utils.fromShortDate
import java.util.*

enum class DayOfWeek(private val res: Int, val color: Color) {
    Monday(R.string.monday, Color.Black),
    Tuesday(R.string.tuesday, Color.Black),
    Wednesday(R.string.wednesday, Color.Black),
    Thursday(R.string.thursday, Color.Black),
    Friday(R.string.friday, Color.Black),
    Saturday(R.string.saturday, Color.Red),
    Sunday(R.string.sunday, Color.Red);

    @Composable
    val label: String
        get() = stringResource(res)
}

enum class Month(private val res: Int, val code: String) {
    January(R.string.january, "01"),
    February(R.string.february, "02"),
    March(R.string.march, "03"),
    April(R.string.april, "04"),
    May(R.string.may, "05"),
    June(R.string.june, "06"),
    July(R.string.july, "07"),
    August(R.string.august, "08"),
    September(R.string.september, "09"),
    October(R.string.october, "10"),
    November(R.string.november, "11"),
    December(R.string.december, "12");

    @Composable
    val label: String
        get() = stringResource(res)
}

data class Week(
    val dates: List<Day> = listOf()
)

data class Day(
    val dayOfWeek: DayOfWeek,
    val date: String,
    val week: Int,
    val selected: Boolean
)

class CalendarModel(day: String = "") {
    private var calendar: Calendar = Calendar.getInstance()

    var selected: String = ""
    var selectedMonth: Month = Month.January
    var selectedYear: String = ""
    var selectedDay: String = ""

    var headerYear: String = "2020"
    var headerMonth: Month = Month.January

    var years: List<String> = listOf()
    var weeks: List<Week> = listOf()

    init {
        if (day.isEmpty()) {
            calendar.time = Date()
        } else {
            calendar.time = day.fromShortDate()
        }

        selectedDay = calendar.get(Calendar.DAY_OF_MONTH).toString()
        selectedMonth = Month.values()[calendar.get(Calendar.MONTH)]
        selectedYear = calendar.get(Calendar.YEAR).toString()

        calendar.set(Calendar.HOUR, 1)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        init(calendar = calendar)
    }

    fun nextMonth() {
        calendar.add(Calendar.MONTH, 1)

        init(calendar)
    }

    fun previousMonth() {
        calendar.add(Calendar.MONTH, -1)

        init(calendar)
    }

    fun onSelectedMonth(month: Month) {
        calendar.set(Calendar.MONTH, month.ordinal)

        init(calendar)
    }

    fun onSelectedYear(year: String) {
        calendar.set(Calendar.YEAR, year.toInt())

        init(calendar)
    }

    fun onSelectedDay(day: Day, set: (String) -> Unit) {
        this.selectedDay = day.date
        this.selectedMonth = headerMonth
        this.selectedYear = headerYear

        init(calendar)

        set(this.selected)
    }

    private fun init(calendar: Calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val days = mutableListOf<Day>()

        while (month == calendar.get(Calendar.MONTH)) {
            val week = calendar.get(Calendar.WEEK_OF_MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
                Calendar.MONDAY -> DayOfWeek.Monday
                Calendar.TUESDAY -> DayOfWeek.Tuesday
                Calendar.WEDNESDAY -> DayOfWeek.Wednesday
                Calendar.THURSDAY -> DayOfWeek.Thursday
                Calendar.FRIDAY -> DayOfWeek.Friday
                Calendar.SATURDAY -> DayOfWeek.Saturday
                Calendar.SUNDAY -> DayOfWeek.Sunday
                else -> throw IllegalArgumentException("Unknown day of week (${calendar.get(Calendar.DAY_OF_WEEK)})")
            }

            val isSelected = selectedDay == "$day" && selectedMonth == Month.values()[month] && selectedYear == "$year"

            days.add(Day(dayOfWeek, "$day", week, isSelected))

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        calendar.add(Calendar.DAY_OF_MONTH, -1)

        this.selected = "${selectedDay}.${selectedMonth.code}.${selectedYear}"
        this.headerYear = "$year"
        this.headerMonth = Month.values()[month]
        this.weeks = days.groupBy { it.week }.map { Week(it.value) }
        this.years = (year - 5..year + 5).map { "$it" }
    }
}
