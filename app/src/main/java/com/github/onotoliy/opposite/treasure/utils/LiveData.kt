package com.github.onotoliy.opposite.treasure.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData

@Composable
fun <T> mutableStateOf(
    default: T,
    loading: () -> LiveData<T>
): MutableState<T> = remember(default) {
    mutableStateOf(default).apply {
        loading().observeForever { value = it }
    }
}

@Composable
fun <T> mutableStateOf(default: List<T>, loading: (Int, Int) -> LiveData<List<T>>): MutableState<List<T>> =
    mutableStateOf(default, { it }, loading)

@Composable
fun <T, C> mutableStateOf(
    default: List<T>,
    convert: (C) -> T,
    loading: (Int, Int) -> LiveData<List<C>>
): MutableState<List<T>> = remember(default) {
    mutableStateOf(default).apply {
        loading(this, convert, loading)
    }
}

fun <T> loading(context: MutableState<List<T>>, loading: (Int, Int) -> LiveData<List<T>>) =
    loading(context, { it }, loading)

fun <T, C> loading(context: MutableState<List<T>>, convert: (C) -> T, loading: (Int, Int) -> LiveData<List<C>>) =
    loading(context.value.size, 2).observeForever { list ->
        context.value = mutableListOf<T>().apply {
            addAll(context.value)
            addAll(list.map { convert(it) })
        }
    }
