package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.DebtHelper
import javax.inject.Inject

class DebtService @Inject constructor(private val helper: DebtHelper) {
    fun getAll(person: String, offset: Int, numberOfRows: Int): Page<Event> = Page()
}
