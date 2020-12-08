package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.treasure.di.database.*

fun CashboxVO?.toDTO() = this?.let {
    Cashbox(deposit, lastUpdateDate)
} ?: Cashbox("0.0", "")

fun Cashbox.toVO() = CashboxVO(deposit, lastUpdateDate)