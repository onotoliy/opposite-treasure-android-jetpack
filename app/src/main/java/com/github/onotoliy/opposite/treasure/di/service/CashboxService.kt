package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.treasure.di.database.*
import com.github.onotoliy.opposite.treasure.di.resource.CashboxResource
import javax.inject.Inject

class CashboxService @Inject constructor(
    private val repository: CashboxRepository,
    private val retrofit: CashboxResource
): AbstractService<Cashbox>(repository) {

    fun get(): Cashbox = repository.get()

    override fun sync() = syncTransactional {
        syncObject { retrofit.get().execute() }
    }
}