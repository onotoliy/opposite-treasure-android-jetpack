package com.github.onotoliy.opposite.treasure.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.activity.model.DepositActivityModel
import com.github.onotoliy.opposite.treasure.activity.model.DepositService
import com.github.onotoliy.opposite.treasure.services.getUUID
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.screens.DepositTab
import com.github.onotoliy.opposite.treasure.ui.screens.views.DepositView
import com.github.onotoliy.opposite.treasure.ui.screens.views.EventPageView
import com.github.onotoliy.opposite.treasure.ui.screens.views.TransactionPageView
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class DepositActivity : AppCompatActivity() {

    @Inject
    lateinit var depositService: DepositService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).appComponent.inject(this)

        val navigateTo: (Screen) -> Unit = { goto(it) }
        val manager: AccountManager = AccountManager.get(applicationContext)
        val screen = DepositActivityModel(
            pk = intent.getStringExtra("pk") ?: manager.getUUID(),
            manager = manager
        )

        screen.loading()

        setContent {
            TreasureTheme {
                Menu(
                    bodyContent = { DepositScreen(screen, navigateTo) },
                    navigateTo = navigateTo
                )
            }
        }
    }
}

@Composable
fun DepositScreen(
    model: DepositActivityModel,
    navigateTo: (Screen) -> Unit
) {
    val selected = remember { mutableStateOf(DepositTab.GENERAL) }

    model.pending.observe()?.let { pending ->
        Column {
            TabRow(
                selectedTabIndex = selected.value.ordinal
            ) {
                DepositTab.values().forEach { tab ->
                    Tab(
                        selected = tab == selected.value,
                        onClick = { selected.value = tab },
                        text = { Text(text = tab.label) }
                    )
                }
            }

            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            when (selected.value) {
                DepositTab.GENERAL -> DepositView(
                    deposit = model.deposit.observe(),
                    cashbox = model.cashbox.observe()
                )
                DepositTab.DEBT -> model.events.observe()?.let {
                    EventPageView(
                        view = PageView(it.offset, it.numberOfRows, it.context),
                        navigateTo = navigateTo,
                        navigateToNextPageScreen = { offset, numberOfRows, _ ->
                            model.nextEventPageLoading(offset, numberOfRows)
                        }
                    )
                }
                DepositTab.TRANSACTION -> model.transactions.observe()?.let {
                    TransactionPageView(
                        view = PageView(it.offset, it.numberOfRows, it.context),
                        navigateTo = navigateTo,
                        navigateToNextPageScreen = { offset, numberOfRows, _ ->
                            model.nextTransactionPageLoading(offset, numberOfRows)
                        }
                    )
                }
            }
        }
    }
}
