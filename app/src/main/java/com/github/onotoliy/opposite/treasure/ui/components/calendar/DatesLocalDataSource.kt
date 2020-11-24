package com.github.onotoliy.opposite.treasure.ui.components.calendar

import javax.inject.Inject
import javax.inject.Singleton

typealias CalendarYear = List<CalendarMonth>

/**
 * Annotated with Singleton because [CalendarDay] contains mutable state.
 */
@Singleton
class DatesLocalDataSource @Inject constructor() {

    private val january2020 = CalendarMonth(
        name = "January",
        year = "2020",
        numDays = 31,
        monthNumber = 1,
        startDayOfWeek = DayOfWeek.Wednesday
    )
    private val february2020 = CalendarMonth(
        name = "February",
        year = "2020",
        numDays = 29,
        monthNumber = 2,
        startDayOfWeek = DayOfWeek.Saturday
    )
    private val march2020 = CalendarMonth(
        name = "March",
        year = "2020",
        numDays = 31,
        monthNumber = 3,
        startDayOfWeek = DayOfWeek.Sunday
    )
    private val april2020 = CalendarMonth(
        name = "April",
        year = "2020",
        numDays = 30,
        monthNumber = 4,
        startDayOfWeek = DayOfWeek.Wednesday
    )
    private val may2020 = CalendarMonth(
        name = "May",
        year = "2020",
        numDays = 31,
        monthNumber = 5,
        startDayOfWeek = DayOfWeek.Friday
    )
    private val june2020 = CalendarMonth(
        name = "June",
        year = "2020",
        numDays = 30,
        monthNumber = 6,
        startDayOfWeek = DayOfWeek.Monday
    )
    private val july2020 = CalendarMonth(
        name = "July",
        year = "2020",
        numDays = 31,
        monthNumber = 7,
        startDayOfWeek = DayOfWeek.Wednesday
    )
    private val august2020 = CalendarMonth(
        name = "August",
        year = "2020",
        numDays = 31,
        monthNumber = 8,
        startDayOfWeek = DayOfWeek.Saturday
    )
    private val september2020 = CalendarMonth(
        name = "September",
        year = "2020",
        numDays = 30,
        monthNumber = 9,
        startDayOfWeek = DayOfWeek.Tuesday
    )
    private val october2020 = CalendarMonth(
        name = "October",
        year = "2020",
        numDays = 31,
        monthNumber = 10,
        startDayOfWeek = DayOfWeek.Thursday
    )
    private val november2020 = CalendarMonth(
        name = "November",
        year = "2020",
        numDays = 30,
        monthNumber = 11,
        startDayOfWeek = DayOfWeek.Sunday
    )
    private val december2020 = CalendarMonth(
        name = "December",
        year = "2020",
        numDays = 31,
        monthNumber = 12,
        startDayOfWeek = DayOfWeek.Tuesday
    )

    val year2020: CalendarYear = listOf(
        january2020,
        february2020,
        march2020,
        april2020,
        may2020,
        june2020,
        july2020,
        august2020,
        september2020,
        october2020,
        november2020,
        december2020
    )
}