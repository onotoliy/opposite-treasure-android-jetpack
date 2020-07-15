package com.github.onotoliy.opposite.treasure.tasks.transaction

import android.accounts.AccountManager
import com.fasterxml.jackson.core.type.TypeReference
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.treasure.tasks.GetRequest

class TransactionTask(manager: AccountManager, uuid: String) : GetRequest<Transaction>(
    link = "http://185.12.95.242/api/treasure/v1/transaction/$uuid",
    manager = manager,
    valueTypeRef = object : TypeReference<Transaction>() {}
)