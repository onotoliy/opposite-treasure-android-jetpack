package com.github.onotoliy.opposite.treasure.ui.components.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CalendarViewModel: ViewModel() {

    val datesSelected = DatesSelectedState(DatesLocalDataSource().year2020)
    val calendarYear = DatesLocalDataSource().year2020

    fun onDaySelected(daySelected: DaySelected) {
        viewModelScope.launch {
            datesSelected.daySelected(daySelected)
        }
    }
}
