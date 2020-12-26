package com.github.onotoliy.opposite.treasure.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> navigateToNextPage(
    context: MutableState<List<T>>,
    getAll: (Int, Int) -> LiveData<List<T>>
) {
    context.v3LoadingAdd(
        get = { getAll(context.value.size, 20) },
        finished = { }
    )
}


fun <T> MutableState<List<T>>.v3LoadingAdd(get: () -> LiveData<List<T>>, finished: () -> Unit) {
    get().observeForever {
        val list = mutableListOf<T>()

        list.addAll(value)
        list.addAll(it)

        value = list
        finished()
    }
}

fun <T> MutableState<T>.v3Loading(get: () -> LiveData<T>) {
    get().observeForever { value = it }
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
fun <T: Any> LiveData<T>.observeAs(defaultValue: T): MutableState<T> {
    val data: LiveData<T> = this
    val result = remember { mutableStateOf(data.value ?: defaultValue) }
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
