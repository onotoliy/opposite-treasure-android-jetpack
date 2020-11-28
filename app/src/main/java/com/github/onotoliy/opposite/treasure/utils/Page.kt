package com.github.onotoliy.opposite.treasure.utils

import com.github.onotoliy.opposite.data.page.Page

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
