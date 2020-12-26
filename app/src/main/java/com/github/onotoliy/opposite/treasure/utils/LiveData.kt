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
fun <T> MutableState<T>.v1Loading(get: () -> LiveData<T>, finished: () -> Unit) {
    val observer = remember {
        Observer<T> {
            this.value = it
            finished()
        }
    }

    val data = get()

    onCommit(data) {
        data.observeForever(observer)
        onDispose { data.removeObserver(observer) }
    }
}

@Composable
fun <T> LiveData<T>.observeFor(state: MutableState<T>, defaultValue: T) {
    val data: LiveData<T> = this

    state.value = data.value ?: defaultValue

    val observer = remember { Observer<T> { state.value = it } }

    onCommit(data) {
        data.observeForever(observer)
        onDispose { data.removeObserver(observer) }
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
