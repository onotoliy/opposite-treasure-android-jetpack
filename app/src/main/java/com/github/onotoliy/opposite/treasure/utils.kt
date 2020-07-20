package com.github.onotoliy.opposite.treasure

import androidx.compose.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.ui.foundation.ScrollerPosition
import com.github.onotoliy.opposite.data.page.Page

fun String.formatDate() = substring(0, 10)

@Composable
fun <T> observe(data: LiveData<T>): T? {
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