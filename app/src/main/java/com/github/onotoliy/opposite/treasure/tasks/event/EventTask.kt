package com.github.onotoliy.opposite.treasure.tasks.event

import android.accounts.AccountManager
import com.fasterxml.jackson.core.type.TypeReference
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.treasure.auth.getUUID
import com.github.onotoliy.opposite.treasure.tasks.GetRequest

class EventTask(manager: AccountManager, uuid: String) : GetRequest<Event>(
    link = "http://185.12.95.242/api/treasure/v1/event/$uuid",
    manager = manager,
    valueTypeRef = object : TypeReference<Event>() {}
)