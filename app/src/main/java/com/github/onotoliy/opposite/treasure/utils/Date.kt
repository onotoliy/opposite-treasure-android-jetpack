package com.github.onotoliy.opposite.treasure.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Date.toShortDate(): String = SimpleDateFormat("dd.MM.yyyy").format(this)

@SuppressLint("SimpleDateFormat")
fun Date.toISO(): String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(this)

@SuppressLint("SimpleDateFormat")
fun String.fromShortDate(): Date = SimpleDateFormat("dd.MM.yyyy").parse(this)!!

@SuppressLint("SimpleDateFormat")
fun String.fromISO(): Date {
    val v: String = if (endsWith("Z")) substring(0, length - 1) else this

    return if (v.endsWith("Z")) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(v)!!
    } else {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(v)!!
    }
}
