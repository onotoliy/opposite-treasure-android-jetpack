package com.github.onotoliy.opposite.treasure.tasks.deposit

import android.accounts.AccountManager
import com.fasterxml.jackson.core.type.TypeReference
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.treasure.auth.getUUID
import com.github.onotoliy.opposite.treasure.tasks.GetRequest

class DepositTask(manager: AccountManager, uuid: String) : GetRequest<Deposit>(
    link = "http://185.12.95.242/api/treasure/v1/deposit/$uuid",
    manager = manager,
    valueTypeRef = object : TypeReference<Deposit>() {}
)