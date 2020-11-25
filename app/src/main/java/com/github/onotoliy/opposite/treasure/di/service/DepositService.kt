package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.DepositRepository
import javax.inject.Inject

class DepositService @Inject constructor(private val helper: DepositRepository) {
    fun get(pk: String): Deposit = helper.get(pk)

    fun getAll(offset: Int, limit: Int): Page<Deposit> = helper.getAll(offset, limit)
}
