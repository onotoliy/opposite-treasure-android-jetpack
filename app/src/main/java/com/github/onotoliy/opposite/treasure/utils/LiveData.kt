package com.github.onotoliy.opposite.treasure.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

@Composable
fun <T> mutableStateOf(default: T, loading: () -> LiveData<T>): MutableState<T> {
    return remember(default) {
        mutableStateOf(default).apply {
            loading().observeForever { value = it }
        }
    }
}

@Composable
fun <T> mutableStateOf(default: List<T>, loading: (Int, Int) -> LiveData<List<T>>): MutableState<List<T>> {
    return mutableStateOf(default, { it }, loading)
}

@Composable
fun <T, C> mutableStateOf(default: List<T>, convert: (C) -> T, loading: (Int, Int) -> LiveData<List<C>>): MutableState<List<T>> {
    return remember(default) {
        mutableStateOf(default).apply {
            loading(this, convert, loading)
        }
    }
}

fun <T> loading(context: MutableState<List<T>>, loading: (Int, Int) -> LiveData<List<T>>) {
    loading(context, { it }, loading)
}

fun <T, C> loading(context: MutableState<List<T>>, convert: (C) -> T, loading: (Int, Int) -> LiveData<List<C>>) {
    loading(context.value.size, 2).observeForever { list ->
        context.value = mutableListOf<T>().apply {
            addAll(context.value)
            addAll(list.map { convert(it) })
        }
    }
}

@Composable
fun <T> LiveData<T>.observeAs(): MutableState<T?> {
    val data: LiveData<T> = this
    val result = remember { mutableStateOf(data.value) }
    val observer = remember { Observer<T> { result.value = it } }

    onCommit(data) {
        data.observeForever(observer)
        onDispose { data.removeObserver(observer) }
    }

    return result
}

@Composable
fun <T> LiveData<T>.observe(defaultValue: T): T {
    val data: LiveData<T> = this
    val result = remember { mutableStateOf(data.value) }
    val observer = remember { Observer<T> { result.value = it } }

    onCommit(data) {
        data.observeForever(observer)
        onDispose { data.removeObserver(observer) }
    }

    return result.value ?: defaultValue
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

data class LiveDataPage<T>(
    var total: MutableLiveData<Long> = MutableLiveData(),
    var offset: Int = 0,
    var numberOfRows: Int = 10,
    var context: MutableLiveData<List<T>> = MutableLiveData(),
)
