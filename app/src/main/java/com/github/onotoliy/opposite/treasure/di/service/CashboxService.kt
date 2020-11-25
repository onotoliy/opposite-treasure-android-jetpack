package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.treasure.di.database.*
import javax.inject.Inject

class CashboxService @Inject constructor(private val helper: CashboxRepository) {
    fun get(): Cashbox = helper.get()
}