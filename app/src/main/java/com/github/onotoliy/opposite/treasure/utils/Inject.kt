package com.github.onotoliy.opposite.treasure.utils

import com.github.onotoliy.opposite.treasure.App
import com.github.onotoliy.opposite.treasure.ui.activity.DepositActivity
import com.github.onotoliy.opposite.treasure.ui.activity.DepositPageActivity
import com.github.onotoliy.opposite.treasure.ui.activity.EventActivity
import com.github.onotoliy.opposite.treasure.ui.activity.EventEditActivity
import com.github.onotoliy.opposite.treasure.ui.activity.EventPageActivity
import com.github.onotoliy.opposite.treasure.ui.activity.ExceptionActivity
import com.github.onotoliy.opposite.treasure.ui.activity.LoadingActivity
import com.github.onotoliy.opposite.treasure.ui.activity.LoginActivity
import com.github.onotoliy.opposite.treasure.ui.activity.TransactionActivity
import com.github.onotoliy.opposite.treasure.ui.activity.TransactionEditActivity
import com.github.onotoliy.opposite.treasure.ui.activity.TransactionPageActivity

fun TransactionActivity.inject() = (application as App).appComponent.inject(this)
fun TransactionEditActivity.inject() = (application as App).appComponent.inject(this)
fun TransactionPageActivity.inject() = (application as App).appComponent.inject(this)

fun EventActivity.inject() = (application as App).appComponent.inject(this)
fun EventEditActivity.inject() = (application as App).appComponent.inject(this)
fun EventPageActivity.inject() = (application as App).appComponent.inject(this)

fun DepositActivity.inject() = (application as App).appComponent.inject(this)
fun DepositPageActivity.inject() = (application as App).appComponent.inject(this)

fun LoginActivity.inject() = (application as App).appComponent.inject(this)
fun LoadingActivity.inject() = (application as App).appComponent.inject(this)
fun ExceptionActivity.inject() = (application as App).appComponent.inject(this)
