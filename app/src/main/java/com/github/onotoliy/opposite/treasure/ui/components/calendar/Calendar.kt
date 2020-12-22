package com.github.onotoliy.opposite.treasure.ui.components.calendar

import android.content.res.Resources
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.utils.fromShortDate
import java.util.*

enum class DayOfWeek(val label: String, val color: Color) {
    Monday(Resources.getSystem().getString(R.string.monday), Color.Black),
    Tuesday(Resources.getSystem().getString(R.string.tuesday), Color.Black),
    Wednesday(Resources.getSystem().getString(R.string.wednesday), Color.Black),
    Thursday(Resources.getSystem().getString(R.string.thursday), Color.Black),
    Friday(Resources.getSystem().getString(R.string.friday), Color.Black),
    Saturday(Resources.getSystem().getString(R.string.saturday), Color.Red),
    Sunday(Resources.getSystem().getString(R.string.sunday), Color.Red)
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
    private val calendar: Calendar = Calendar.getInstance()

    val selected: MutableLiveData<String> = MutableLiveData()
    private val selectedMonth: MutableLiveData<String> = MutableLiveData()
    private val selectedYear: MutableLiveData<String> = MutableLiveData()
    private val selectedDay: MutableLiveData<String> = MutableLiveData()

    val headerYear: MutableLiveData<String> = MutableLiveData("2020")
    val headerMonth: MutableLiveData<String> = MutableLiveData(
        Resources.getSystem().getString(R.string.january)
    )

    val years: MutableLiveData<List<String>> = MutableLiveData()
    val months: List<String> = listOf(
        Resources.getSystem().getString(R.string.january),
        Resources.getSystem().getString(R.string.february),
        Resources.getSystem().getString(R.string.march),
        Resources.getSystem().getString(R.string.april),
        Resources.getSystem().getString(R.string.may),
        Resources.getSystem().getString(R.string.june),
        Resources.getSystem().getString(R.string.july),
        Resources.getSystem().getString(R.string.august),
        Resources.getSystem().getString(R.string.september),
        Resources.getSystem().getString(R.string.october),
        Resources.getSystem().getString(R.string.november),
        Resources.getSystem().getString(R.string.december)
    )

    val weeks: MutableLiveData<List<Week>> = MutableLiveData()

    init {
        if (day.isEmpty()) {
            calendar.time = Date()
        } else {
            calendar.time = day.fromShortDate()
        }

        selectedDay.postValue(calendar.get(Calendar.DAY_OF_MONTH).toString())
        selectedMonth.postValue(months[calendar.get(Calendar.MONTH)])
        selectedYear.postValue(calendar.get(Calendar.YEAR).toString())

        calendar.set(Calendar.HOUR, 1)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        selectedMonth.observeForever { init(calendar) }
        selectedYear.observeForever { init(calendar) }
        selectedDay.observeForever { init(calendar) }

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

    fun onSelectedMonth(month: String) {
        calendar.set(Calendar.MONTH, months.indexOf(month))

        init(calendar)
    }

    fun onSelectedYear(year: String) {
        calendar.set(Calendar.YEAR, year.toInt())

        init(calendar)
    }

    fun onSelectedDay(day: Day) {
        this.selectedDay.postValue(day.date)
        this.selectedMonth.postValue(headerMonth.value)
        this.selectedYear.postValue(headerYear.value)

        init(calendar)
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

            val isSelected =
                selectedDay.value == "$day" && selectedMonth.value == months[month] && selectedYear.value == "$year"

            days.add(Day(dayOfWeek, "$day", week, isSelected))

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        calendar.add(Calendar.DAY_OF_MONTH, -1)

        this.selected.postValue(
            "${selectedDay.value ?: ""}.${months.indexOf(selectedMonth.value ?: "") + 1}.${selectedYear.value ?: ""}"
        )
        this.headerYear.postValue("$year")
        this.headerMonth.postValue(months[month])
        this.weeks.postValue(days.groupBy { it.week }.map { Week(it.value) })
        this.years.postValue((year - 5..year + 5).map { "$it" })
    }
}
