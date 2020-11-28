package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Debt
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.di.database.DebtRepository
import com.github.onotoliy.opposite.treasure.di.resource.DebtResource
import javax.inject.Inject

class DebtService @Inject constructor(
    private val repository: DebtRepository,
    private val retrofit: DebtResource
): AbstractService<Debt>(repository)  {

    fun getDebtorAll(event: String, offset: Int, numberOfRows: Int): Page<Deposit> =
        repository.getDebtorAll(event, offset, numberOfRows)

    fun getDebtAll(deposit: String, offset: Int, numberOfRows: Int): Page<Event> =
        repository.getDebtAll(deposit, offset, numberOfRows)

    override fun sync() = syncTransactional {
        val version = repository.version()

        syncPage { offset, numberOfRows ->
            retrofit.sync(version = version, offset = offset, numberOfRows = numberOfRows).execute()
        }
    }
}
