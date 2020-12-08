package com.github.onotoliy.opposite.treasure.utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.ui.activity.*

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
    }


private fun <T: Any> AppCompatActivity.goto(clazz: Class<T>, options: Intent.() -> Unit = {}) {
    val intent = Intent(applicationContext, clazz).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK

        options()
    }
    applicationContext.startActivity(intent, Bundle())
}
