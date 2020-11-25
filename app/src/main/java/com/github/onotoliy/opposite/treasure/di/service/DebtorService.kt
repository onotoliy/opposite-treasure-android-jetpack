package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.DebtorHelper
import javax.inject.Inject

class DebtorService @Inject constructor(private val helper: DebtorHelper) {
    fun getAll(event: String, offset: Int, limit: Int): Page<Deposit> = Page()
}