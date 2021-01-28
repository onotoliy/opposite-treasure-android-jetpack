package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import com.github.onotoliy.opposite.treasure.ui.IconAdd
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.TransactionPageView
import com.github.onotoliy.opposite.treasure.utils.defaultTransactions
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.isAdministrator
import com.github.onotoliy.opposite.treasure.utils.loading
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import javax.inject.Inject

class TransactionPageActivity : AppCompatActivity() {

    @Inject
    lateinit var dao: TransactionRepository

    @Inject
    lateinit var account: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        setContent {
            val total = mutableStateOf(0L, dao::count)
            val context = mutableStateOf(defaultTransactions) { o, n -> dao.getAll(o, n) }

            TreasureTheme {
                Menu(
                    screen = Screen.TransactionPageScreen,
                    floatingActionButton = {
                        if (account.isAdministrator()) {
                            FloatingActionButton(
                                modifier = Modifier.border(1.dp, Color.LightGray, CircleShape),
                                backgroundColor = Color.White,
                                content = { IconAdd() },
                                onClick = { navigateTo(Screen.TransactionEditScreen()) }
                            )
                        }
                    },
                    bodyContent = {
                        TransactionPageView(
                            list = context.value,
                            total = total.value,
                            navigateTo = ::navigateTo,
                            navigateToNextPageScreen = { loading(context) { o, n -> dao.getAll(o, n) } }
                        )
                    },
                    navigateTo = ::navigateTo
                )
            }
        }
    }
}
