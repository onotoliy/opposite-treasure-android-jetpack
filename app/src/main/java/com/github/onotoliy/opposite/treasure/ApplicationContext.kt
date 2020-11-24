package com.github.onotoliy.opposite.treasure

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.onotoliy.opposite.treasure.activity.*
import dagger.android.support.DaggerAppCompatActivity

fun <T: Any> AppCompatActivity.goto(clazz: Class<T>, options: Intent.() -> Unit = {}) {
    val intent = Intent(applicationContext, clazz).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK

        options()
    }
    applicationContext.startActivity(intent, Bundle())
}

fun AppCompatActivity.goto(screen: Screen) {
    when(screen) {
        is Screen.LoginScreen -> goto(LoginActivity::class.java)
        is Screen.DepositPageScreen -> goto(DepositPageActivity::class.java)
        is Screen.EventPageScreen -> goto(EventPageActivity::class.java)
        is Screen.TransactionPageScreen -> goto(TransactionPageActivity::class.java)
        is Screen.EventScreen -> goto(EventActivity::class.java) {
            putExtra("pk", screen.pk)
        }
        is Screen.TransactionScreen -> goto(TransactionActivity::class.java) {
            putExtra("pk", screen.pk)
        }
        is Screen.DepositScreen -> goto(DepositActivity::class.java) {
            putExtra("pk", screen.pk)
        }
        else -> throw IllegalArgumentException()
    }
}

fun bundle(block: Bundle.() -> Unit): Bundle {
    return Bundle().apply { block() }
}

fun <T: Any> DaggerAppCompatActivity.goto(clazz: Class<T>, options: Intent.() -> Unit = {}) {
    val intent = Intent(applicationContext, clazz).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK

        options()
    }
    applicationContext.startActivity(intent, Bundle())
}

fun DaggerAppCompatActivity.goto(screen: Screen) {
    when(screen) {
        is Screen.LoginScreen -> goto(LoginActivity::class.java)
        is Screen.DepositPageScreen -> goto(DepositPageActivity::class.java)
        is Screen.EventPageScreen -> goto(EventPageActivity::class.java)
        is Screen.TransactionPageScreen -> goto(TransactionPageActivity::class.java)
        is Screen.EventScreen -> goto(EventActivity::class.java) {
            putExtra("pk", screen.pk)
        }
        is Screen.TransactionScreen -> goto(TransactionActivity::class.java) {
            putExtra("pk", screen.pk)
        }
        is Screen.DepositScreen -> goto(DepositActivity::class.java) {
            putExtra("pk", screen.pk)
        }
        else -> throw IllegalArgumentException()
    }
}