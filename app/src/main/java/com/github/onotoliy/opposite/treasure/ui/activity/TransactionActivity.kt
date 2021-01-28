package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import com.github.onotoliy.opposite.treasure.ui.IconEdit
import com.github.onotoliy.opposite.treasure.ui.IconRemove
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.TransactionView
import com.github.onotoliy.opposite.treasure.utils.DELETE
import com.github.onotoliy.opposite.treasure.utils.INSERT
import com.github.onotoliy.opposite.treasure.utils.defaultTransaction
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.isAdministrator
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.pk
import java.util.concurrent.Executors
import javax.inject.Inject

class TransactionActivity : AppCompatActivity() {

    @Inject
    lateinit var dao: TransactionRepository

    @Inject
    lateinit var account: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pk = intent.pk ?: throw IllegalArgumentException("Primary key can not be null")

        inject()

        setContent {
            val context = mutableStateOf(defaultTransaction) { dao.get(pk) }

            TreasureTheme {
                Menu(
                    screen = Screen.TransactionScreen(pk),
                    actions = {
                        if (context.value.local == INSERT && account.isAdministrator()) {
                            IconButton(onClick = {
                                delete(context.value)
                            } ) {
                                IconRemove()
                            }
                        }
                    },
                    floatingActionButton = {
                        if (account.isAdministrator()) {
                            FloatingActionButton(
                                modifier = Modifier.border(1.dp, Color.LightGray, CircleShape),
                                backgroundColor = Color.White,
                                content = { IconEdit() },
                                onClick = { navigateTo(Screen.TransactionEditScreen(pk)) }
                            )
                        }
                    },
                    bodyContent = {
                        TransactionView(dto = context.value, navigateTo = ::navigateTo)
                    },
                    navigateTo = ::navigateTo
                )
            }
        }
    }

    private fun delete(context: TransactionVO) {
        Executors.newSingleThreadExecutor().execute {
            context.local = DELETE

            dao.replace(context)

            navigateTo(Screen.TransactionPageScreen)
        }
    }
}
