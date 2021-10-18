package com.github.onotoliy.opposite.treasure.utils

import android.content.Intent
import android.os.Bundle
import android.os.Process
import androidx.appcompat.app.AppCompatActivity
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.ui.activity.DepositActivity
import com.github.onotoliy.opposite.treasure.ui.activity.DepositPageActivity
import com.github.onotoliy.opposite.treasure.ui.activity.EventActivity
import com.github.onotoliy.opposite.treasure.ui.activity.EventEditActivity
import com.github.onotoliy.opposite.treasure.ui.activity.EventPageActivity
import com.github.onotoliy.opposite.treasure.ui.activity.ExceptionActivity
import com.github.onotoliy.opposite.treasure.ui.activity.LoadingActivity
import com.github.onotoliy.opposite.treasure.ui.activity.LoginActivity
import com.github.onotoliy.opposite.treasure.ui.activity.TransactionActivity
import com.github.onotoliy.opposite.treasure.ui.activity.TransactionEditActivity
import com.github.onotoliy.opposite.treasure.ui.activity.TransactionPageActivity
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

fun AppCompatActivity.setDefaultUncaughtExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
        navigateTo(Screen.ExceptionScreen(throwable, javaClass.canonicalName ?: "Unknown activity"))

        Process.killProcess(Process.myPid())
        exitProcess(0)
    }
}

fun AppCompatActivity.navigateTo(screen: Screen) =
    when(screen) {
        is Screen.LoginScreen -> goto(LoginActivity::class.java)
        is Screen.LoadingScreen -> goto(LoadingActivity::class.java)
        is Screen.EventPageScreen -> goto(EventPageActivity::class.java)
        is Screen.DepositPageScreen -> goto(DepositPageActivity::class.java)
        is Screen.TransactionPageScreen -> goto(TransactionPageActivity::class.java)
        is Screen.EventScreen -> goto(EventActivity::class.java) {
            putExtra("pk", screen.pk)
        }
        is Screen.EventEditScreen -> goto(EventEditActivity::class.java) {
            putExtra("pk", screen.pk)
        }
        is Screen.TransactionScreen -> goto(TransactionActivity::class.java) {
            putExtra("pk", screen.pk)
        }
        is Screen.TransactionEditScreen -> goto(TransactionEditActivity::class.java) {
            putExtra("pk", screen.pk)
        }
        is Screen.DepositScreen -> goto(DepositActivity::class.java) {
            putExtra("pk", screen.pk)
        }
        is Screen.ExceptionScreen -> goto(ExceptionActivity::class.java) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            screen.throwable.printStackTrace(pw)

            putExtra("message", screen.throwable.message)
            putExtra("localizedMessage", screen.throwable.localizedMessage)
            putExtra("stackTrace", sw.toString())
            putExtra("previousScreen", screen.previousScreen)

            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

private fun <T: Any> AppCompatActivity.goto(clazz: Class<T>, options: Intent.() -> Unit = {}) {
    val intent = Intent(applicationContext, clazz).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK

        options()
    }
    applicationContext.startActivity(intent, Bundle())
}
