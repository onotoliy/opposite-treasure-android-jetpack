package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.DepositRepository
import com.github.onotoliy.opposite.treasure.di.resource.DepositResource
import javax.inject.Inject

class DepositService @Inject constructor(
    private val repository: DepositRepository,
    private val retrofit: DepositResource
): AbstractService<Deposit>(repository) {

    fun get(pk: String): Deposit = repository.get(pk)

    fun getAll(offset: Int, limit: Int): Page<Deposit> = repository.getAll(offset, limit)

    override fun sync() = syncTransactional {
        val version = repository.version()

        syncPage { offset, numberOfRows ->
            retrofit.sync(version = version, offset = offset, numberOfRows = numberOfRows).execute()
        }
    }
}
