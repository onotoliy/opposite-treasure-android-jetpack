package com.github.onotoliy.opposite.treasure.di.service

import androidx.lifecycle.LiveData
import androidx.work.impl.utils.LiveDataUtils
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import com.github.onotoliy.opposite.treasure.di.database.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.DepositVO
import com.github.onotoliy.opposite.treasure.di.resource.DepositResource
import com.github.onotoliy.opposite.treasure.utils.observe
import javax.inject.Inject

fun DepositVO.toDTO() =
    Deposit(
        person = Option(uuid, name),
        deposit = deposit
    )

fun Deposit.toVO() = DepositVO(
    uuid = uuid,
    name = name,
    deposit = deposit,
)