package com.github.onotoliy.opposite.treasure.ui.activity

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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.di.model.DepositActivityModel
import com.github.onotoliy.opposite.treasure.di.service.toDTO
import com.github.onotoliy.opposite.treasure.di.worker.*
import com.github.onotoliy.opposite.treasure.utils.getUUID
import com.github.onotoliy.opposite.treasure.utils.observe
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.DepositView
import com.github.onotoliy.opposite.treasure.ui.views.EventPageView
import com.github.onotoliy.opposite.treasure.ui.views.TransactionPageView
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.pk
import javax.inject.Inject

class DepositActivity : AppCompatActivity() {

    @Inject lateinit var model: DepositActivityModel

    @Inject lateinit var account: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        val manager = WorkManager.getInstance(applicationContext)

        val debt: WorkRequest = OneTimeWorkRequestBuilder<DebtWorker>().build()
        val event: WorkRequest = OneTimeWorkRequestBuilder<EventWorker>().build()
        val transaction: WorkRequest = OneTimeWorkRequestBuilder<TransactionWorker>().build()
        val deposit: WorkRequest = OneTimeWorkRequestBuilder<DepositWorker>().build()

        val operation = manager.enqueue(listOf(debt, event, transaction, deposit))

        model.loading(intent.pk ?: account.getUUID())

        setContent {
            TreasureTheme {
                Menu(
                    bodyContent = { DepositScreen(model, ::navigateTo) },
                    navigateTo = ::navigateTo
                )
            }
        }
    }
}

enum class DepositTab(val label: String) {
    GENERAL("Общее"),
    DEBT("Долги"),
    TRANSACTION("Операции")
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
                        view = it,
                        navigateTo = navigateTo,
                        navigateToNextPageScreen = { offset, numberOfRows, _ ->
                            model.nextEventPageLoading(offset, numberOfRows)
                        }
                    )
                }
                DepositTab.TRANSACTION -> model.transactions.observe()?.let {
                    TransactionPageView(
                        view = it,
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
