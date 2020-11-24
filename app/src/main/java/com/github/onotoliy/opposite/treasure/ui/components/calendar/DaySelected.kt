package com.github.onotoliy.opposite.treasure.ui.components.calendar

data class DaySelected(val day: Int, val month: CalendarMonth, val year: CalendarYear) {
    val calendarDay = lazy {
        month.getDay(day)
    }

    override fun toString(): String {
        return "${month.name.substring(0, 3).capitalize()} $day"
    }

    operator fun compareTo(other: DaySelected): Int {
        if (day == other.day && month == other.month) return 0
        if (month == other.month) return day.compareTo(other.day)
        return (year.indexOf(month)).compareTo(
            year.indexOf(other.month)
        )
    }
}

/**
 * Represents an empty value for [DaySelected]
 */
val DaySelectedEmpty = DaySelected(-1, CalendarMonth("", "", 0, 0, DayOfWeek.Sunday), emptyList())