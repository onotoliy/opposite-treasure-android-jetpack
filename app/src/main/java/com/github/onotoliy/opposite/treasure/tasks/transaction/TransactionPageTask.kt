package com.github.onotoliy.opposite.treasure.tasks.transaction

import android.accounts.AccountManager
import com.fasterxml.jackson.core.type.TypeReference
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.tasks.GetRequest

class TransactionPageTask(
    manager: AccountManager,
    offset: Int = 0,
    numberOfRows: Int = 20
) : GetRequest<Page<Transaction>>(
    link = "http://185.12.95.242/api/treasure/v1/transaction?offset=$offset&numberOfRows=$numberOfRows",
    manager = manager,
    valueTypeRef = object : TypeReference<Page<Transaction>>() {}
)