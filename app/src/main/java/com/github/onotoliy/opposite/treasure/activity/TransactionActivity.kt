package com.github.onotoliy.opposite.treasure.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.IconEdit
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.activity.model.TransactionActivityModel
import com.github.onotoliy.opposite.treasure.goto
import com.github.onotoliy.opposite.treasure.observe
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.screens.views.TransactionView

class TransactionActivity: AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pk = intent?.getStringExtra("pk") ?: ""
        val navigateTo: (Screen) -> Unit = { goto(it) }
        val manager: AccountManager = AccountManager.get(applicationContext)
        val screen = TransactionActivityModel(pk = pk, manager = manager)

        screen.loading()

        setContent {
            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            icon = { IconEdit() },
                            onClick = { }
                        )
                    },
                    bodyContent = { TransactionScreen(screen, navigateTo) },
                    navigateTo = navigateTo
                )
            }
        }
    }
}

@Composable
fun TransactionScreen(model: TransactionActivityModel, navigateTo: (Screen) -> Unit) {
    model.pending.observe()?.let { pending ->
        Column {
            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            model.transaction.observe()?.let {
                TransactionView(data = it, navigateTo = navigateTo)
            }
        }
    }
}