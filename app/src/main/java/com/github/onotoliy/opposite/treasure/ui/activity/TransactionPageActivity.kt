package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import com.github.onotoliy.opposite.treasure.ui.IconAdd
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.TransactionPageViewVO
import com.github.onotoliy.opposite.treasure.utils.defaultTransactions
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.navigateToNextPage
import com.github.onotoliy.opposite.treasure.utils.v3Loading
import javax.inject.Inject

class TransactionPageActivity : AppCompatActivity() {

    @Inject
    lateinit var dao: TransactionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        setContent {
            val total = remember(0L) {
                mutableStateOf(0L).apply {
                    v3Loading(dao::count)
                }
            }
            val context = remember(defaultTransactions) {
                mutableStateOf(defaultTransactions).apply {
                    navigateToNextPage(this, dao::getAll)
                }
            }

            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            content = { IconAdd() },
                            onClick = { navigateTo(Screen.TransactionEditScreen()) }
                        )
                    },
                    bodyContent = {
                        Column {
                            TransactionPageViewVO(
                                list = context.value,
                                total = total.value,
                                navigateTo = ::navigateTo,
                                navigateToNextPageScreen = { navigateToNextPage(context, dao::getAll) }
                            )
                        }
                    },
                    navigateTo = ::navigateTo
                )
            }
        }
    }
}
