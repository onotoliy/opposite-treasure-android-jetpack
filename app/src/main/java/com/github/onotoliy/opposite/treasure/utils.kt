package com.github.onotoliy.opposite.treasure

import androidx.compose.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.ui.foundation.Icon
import androidx.ui.foundation.ScrollerPosition
import androidx.ui.graphics.Color
import androidx.ui.res.vectorResource
import com.github.onotoliy.opposite.data.page.Page

const val ACCOUNT_TYPE = "com.github.onotoliy.opposite.treasure"
const val DEFAULT_NOTIFICATION_CHANEL_ID = "Nilesh_channel"
const val DEFAULT_NOTIFICATION_CHANEL_NAME = "Notification"

@Composable
fun IconAccountCircle() = Icon(asset = vectorResource(id = R.drawable.ic_account_circle))

@Composable
fun IconAdd() = Icon(asset = vectorResource(id = R.drawable.ic_add))

@Composable
fun IconHome() = Icon(asset = vectorResource(id = R.drawable.ic_home))

@Composable
fun IconDeposits() = Icon(asset = vectorResource(id = R.drawable.ic_people))

@Composable
fun IconTransactions() = Icon(asset = vectorResource(id = R.drawable.ic_payments))

@Composable
fun IconEvents() = Icon(asset = vectorResource(id = R.drawable.ic_event))

@Composable
fun IconRefresh() = Icon(asset = vectorResource(id = R.drawable.ic_refresh))

@Composable
fun IconSave() = Icon(asset = vectorResource(id = R.drawable.ic_save))

@Composable
fun IconEdit() = Icon(asset = vectorResource(id = R.drawable.ic_edit))

@Composable
fun IconRemove() = Icon(asset = vectorResource(id = R.drawable.ic_delete))

@Composable
fun IconUp() = Icon(asset = vectorResource(id = R.drawable.ic_trending_up), tint = Color.Green)

@Composable
fun IconDown() = Icon(asset = vectorResource(id = R.drawable.ic_trending_down), tint = Color.Red)

fun String.formatDate() = substring(0, 10)

data class PageView<T>(
    val offset: Int = 0,
    val numberOfRows: Int = 10,
    val context: Page<T>? = null,
    val default: Page<T>? = null
) {
    @Composable
    val scrollerPosition: ScrollerPosition
        get() = default.scrollerPosition
}

@Composable
fun <T> LiveData<T>.observe(): T? {
    val data: LiveData<T> = this
    var result by state { data.value }
    val observer = remember { Observer<T> { result = it } }

    onCommit(data) {
        data.observeForever(observer)
        onDispose { data.removeObserver(observer) }
    }

    return result
}

@Composable
val <T> Page<T>?.scrollerPosition
    get() = this?.context?.let { items ->
        if (items.size < 10) ScrollerPosition() else ScrollerPosition((100 * items.size).toFloat())
    } ?: ScrollerPosition()

val <T> Page<T>?.offset
   get() = this?.context?.size ?: 0

val <T> Page<T>?.numberOfRows
    get() = this?.meta?.paging?.size ?: 10

val <T> Page<T>?.size
    get() = this?.context?.size ?: 0