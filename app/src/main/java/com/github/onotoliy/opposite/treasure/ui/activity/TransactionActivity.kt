package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.di.model.TransactionActivityModel
import com.github.onotoliy.opposite.treasure.di.service.TransactionService
import com.github.onotoliy.opposite.treasure.ui.IconEdit
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.TransactionView
import javax.inject.Inject

class TransactionActivity: AppCompatActivity()  {

    @Inject
    lateinit var model: TransactionActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).appComponent.inject(this)

        val pk = intent?.getStringExtra("pk") ?: ""
        val navigateTo: (Screen) -> Unit = { goto(it) }

        model.loading(pk)

        setContent {
            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            icon = { IconEdit() },
                            onClick = { }
                        )
                    },
                    bodyContent = { TransactionScreen(model, navigateTo) },
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