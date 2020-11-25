package com.github.onotoliy.opposite.treasure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.github.onotoliy.opposite.data.page.Page

const val ACCOUNT_TYPE = "com.github.onotoliy.opposite.treasure"
const val DEFAULT_NOTIFICATION_CHANEL_ID = "channel_id"
const val DEFAULT_NOTIFICATION_CHANEL_NAME = "channel_name"

fun String.formatDate() = substring(0, 10)

enum class EventTab(val label: String) {
    GENERAL("Общее"),
    DEBTORS("Должники"),
    TRANSACTIONS("Операции")
}

enum class DepositTab(val label: String) {
    GENERAL("Общее"),
    DEBT("Долги"),
    TRANSACTION("Операции")
}

@Composable
fun <T> LiveData<T>.observe(): T? {
    val data: LiveData<T> = this
    val result = remember { mutableStateOf(data.value) }
    val observer = remember { Observer<T> { result.value = it } }

    onCommit(data) {
        data.observeForever(observer)
        onDispose { data.removeObserver(observer) }
    }

    return result.value
}

fun <T> Page<T>?.concat(other: Page<T>): Page<T> =
    Page(
        meta = other.meta,
        context = mutableListOf<T>().apply {
            addAll(this@concat?.context ?: listOf())
            addAll(other.context)
        }
    )

val <T> Page<T>?.offset
   get() = this?.context?.size ?: 0

val <T> Page<T>?.numberOfRows
    get() = this?.meta?.paging?.size ?: 10

val <T> Page<T>?.size
    get() = this?.context?.size ?: 0
