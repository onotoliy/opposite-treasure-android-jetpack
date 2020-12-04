package com.github.onotoliy.opposite.treasure.di.service

import androidx.lifecycle.LiveData
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.treasure.di.database.*
import com.github.onotoliy.opposite.treasure.di.resource.CashboxResource
import com.github.onotoliy.opposite.treasure.utils.observe
import javax.inject.Inject

class CashboxService @Inject constructor(
    private val dao: CashboxDAO,
    private val retrofit: CashboxResource
): AbstractService<Cashbox>() {

    fun get(): LiveData<CashboxVO?> = dao.get()

    override fun sync() = syncObject { retrofit.get().execute() }

    override fun replace(dto: Cashbox) {
        dao.replace(dto.toVO())
    }


}

fun CashboxVO?.toDTO() = this?.let {
    Cashbox(deposit, lastUpdateDate)
} ?: Cashbox("0.0", "")

fun Cashbox.toVO() = CashboxVO(deposit, lastUpdateDate)