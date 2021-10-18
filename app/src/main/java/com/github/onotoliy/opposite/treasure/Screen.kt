package com.github.onotoliy.opposite.treasure

import java.lang.Exception

sealed class Screen {
    object LoginScreen : Screen()

    object LoadingScreen : Screen()

    object DepositPageScreen : Screen()

    object EventPageScreen : Screen()

    object TransactionPageScreen : Screen()

    data class EventScreen(val pk: String) : Screen()

    data class TransactionScreen(val pk: String) : Screen()

    data class DepositScreen(val pk: String? = null) : Screen()

    data class EventEditScreen(val pk: String? = null) : Screen()

    data class TransactionEditScreen(val pk: String? = null) : Screen()

    data class ExceptionScreen(val throwable: Throwable, val previousScreen: String) : Screen()
}
