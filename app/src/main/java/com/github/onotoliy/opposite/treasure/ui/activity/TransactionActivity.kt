package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.model.TransactionActivityModel
import com.github.onotoliy.opposite.treasure.ui.IconEdit
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.TransactionView
import com.github.onotoliy.opposite.treasure.utils.defaultTransaction
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.pk
import com.github.onotoliy.opposite.treasure.utils.v1Loading
import javax.inject.Inject

class TransactionActivity: AppCompatActivity()  {

    @Inject lateinit var model: TransactionActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        model.loading(intent.pk ?: throw IllegalArgumentException("Primary key can not be null"))

        setContent {
            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            content = { IconEdit() },
                            onClick = { }
                        )
                    },
                    bodyContent = { TransactionScreen(model, ::navigateTo) },
                    navigateTo = ::navigateTo
                )
            }
        }
    }
}

@Composable
fun TransactionScreen(model: TransactionActivityModel, navigateTo: (Screen) -> Unit) {
    val pending = remember(true) { mutableStateOf(true) }
    val transaction = remember(defaultTransaction) { mutableStateOf(defaultTransaction) }

    transaction.v1Loading(model::transaction) { pending.value = false }

    Column {
        if (pending.value) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        TransactionView(data = transaction.value, navigateTo = navigateTo)
    }
}


