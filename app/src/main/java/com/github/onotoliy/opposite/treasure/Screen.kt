package com.github.onotoliy.opposite.treasure

import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Transaction

sealed class Screen {
    object LoginScreen : Screen()
    object HomeScreen : Screen()
    object DepositPageScreen : Screen()
    data class EventPageScreen(
        val offset: Int = 0,
        val numberOfRows: Int = 5,
        val default: List<Event>? = listOf()
    ) : Screen()
    data class TransactionPageScreen(
        val offset: Int = 0,
        val numberOfRows: Int = 5,
        val default: List<Transaction>? = listOf()
    ) : Screen()
    data class EventScreen(val event: String) : Screen()
    data class DepositScreen(val deposit: String) : Screen()
    data class TransactionScreen(val transaction: String) : Screen()
}
