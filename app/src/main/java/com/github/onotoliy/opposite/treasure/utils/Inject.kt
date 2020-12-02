package com.github.onotoliy.opposite.treasure.utils

import com.github.onotoliy.opposite.treasure.App
import com.github.onotoliy.opposite.treasure.ui.activity.*

fun TransactionActivity.inject() = (application as App).appComponent.inject(this)
fun TransactionEditActivity.inject() = (application as App).appComponent.inject(this)
fun TransactionPageActivity.inject() = (application as App).appComponent.inject(this)
fun EventActivity.inject() = (application as App).appComponent.inject(this)
fun EventEditActivity.inject() = (application as App).appComponent.inject(this)
fun EventPageActivity.inject() = (application as App).appComponent.inject(this)
fun DepositActivity.inject() = (application as App).appComponent.inject(this)
fun DepositPageActivity.inject() = (application as App).appComponent.inject(this)
fun LoginActivity.inject() = (application as App).appComponent.inject(this)