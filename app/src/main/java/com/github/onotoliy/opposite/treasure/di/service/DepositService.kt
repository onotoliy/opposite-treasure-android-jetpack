package com.github.onotoliy.opposite.treasure.di.service

import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.treasure.di.database.DepositVO

fun DepositVO.toDTO() =
    Deposit(
        uuid = uuid,
        name = name,
        deposit = deposit
    )

fun Deposit.toVO() = DepositVO(
    uuid = uuid,
    name = name,
    deposit = deposit,
)