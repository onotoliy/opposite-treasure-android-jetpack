package com.github.onotoliy.opposite.treasure

import androidx.compose.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.ui.foundation.ScrollerPosition
import com.github.onotoliy.opposite.data.page.Page

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