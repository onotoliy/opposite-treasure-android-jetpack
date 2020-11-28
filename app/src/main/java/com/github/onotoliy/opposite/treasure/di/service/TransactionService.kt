package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.TransactionRepository
import com.github.onotoliy.opposite.treasure.di.resource.TransactionResource
import javax.inject.Inject

class TransactionService @Inject constructor(
    private val repository: TransactionRepository,
    private val retrofit: TransactionResource
): AbstractService<Transaction>(repository)  {
    fun get(pk: String): Transaction = repository.get(pk)

    fun getAll(
        event: String? = null, person: String? = null, offset: Int = 0, numberOfRows: Int = 20
    ): Page<Transaction> = repository.getAll(event, person, offset, numberOfRows)

    override fun sync() = syncTransactional {
        val version = repository.version()

        syncPage { offset, numberOfRows ->
            retrofit.sync(version = version, offset = offset, numberOfRows = numberOfRows).execute()
        }
    }
}
