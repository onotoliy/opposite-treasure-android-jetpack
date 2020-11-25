package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.TransactionRepository
import javax.inject.Inject

class TransactionService @Inject constructor(private val helper: TransactionRepository) {
    fun get(pk: String): Transaction = helper.get(pk)

    fun getAll(
        event: String? = null, person: String? = null, offset: Int = 0, numberOfRows: Int = 20
    ): Page<Transaction> = helper.getAll(event, person, offset, numberOfRows)
}
