package com.github.onotoliy.opposite.treasure.activity.model

import android.content.Context
import javax.inject.Inject

class DepositService @Inject constructor(private val context: Context) {
    fun hello(): String {
        return context.toString()
    }
}