package com.github.onotoliy.opposite.treasure.models

import androidx.compose.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

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