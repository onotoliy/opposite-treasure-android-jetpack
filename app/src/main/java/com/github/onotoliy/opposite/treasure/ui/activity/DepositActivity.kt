package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.CashboxVO
import com.github.onotoliy.opposite.treasure.di.database.data.DebtVO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.CashboxRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.DebtRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.DepositRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import com.github.onotoliy.opposite.treasure.di.restful.resource.NotificationResource
import com.github.onotoliy.opposite.treasure.ui.IconCached
import com.github.onotoliy.opposite.treasure.ui.IconChat
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.DepositView
import com.github.onotoliy.opposite.treasure.ui.views.EventPageView
import com.github.onotoliy.opposite.treasure.ui.views.TransactionPageView
import com.github.onotoliy.opposite.treasure.utils.defaultCashbox
import com.github.onotoliy.opposite.treasure.utils.defaultDeposit
import com.github.onotoliy.opposite.treasure.utils.defaultEvents
import com.github.onotoliy.opposite.treasure.utils.defaultTransactions
import com.github.onotoliy.opposite.treasure.utils.getUUID
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.isAdministrator
import com.github.onotoliy.opposite.treasure.utils.loading
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.pk
import com.github.onotoliy.opposite.treasure.utils.setDefaultUncaughtExceptionHandler
import javax.inject.Inject

class DepositActivity : AppCompatActivity() {

    @Inject
    lateinit var account: AccountManager

    @Inject
    lateinit var notification: NotificationResource

    @Inject
    lateinit var transactions: TransactionRepository

    @Inject
    lateinit var deposit: DepositRepository

    @Inject
    lateinit var cashbox: CashboxRepository

    @Inject
    lateinit var debts: DebtRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        setDefaultUncaughtExceptionHandler()

        val pk = intent.pk ?: account.getUUID()

        setContent {
            val deposit = mutableStateOf(defaultDeposit) { deposit.get(pk) }
            val cashbox = mutableStateOf(defaultCashbox, cashbox::get)
            val totalDebts = mutableStateOf(0L) { debts.countByPerson(pk) }
            val contextDebts = mutableStateOf(defaultEvents, DebtVO::event) { o, n ->
                debts.getByPersonAll(pk, o, n)
            }
            val totalTransactions = mutableStateOf(0L) { transactions.countByEvent(pk) }
            val contextTransactions = mutableStateOf(defaultTransactions) { o, n ->
                transactions.getByPersonAll(pk, o, n)
            }

            TreasureTheme {
                Menu(
                    screen = Screen.DepositScreen(pk),
                    actions = {
                        IconButton(onClick = { navigateTo(Screen.LoadingScreen) }) {
                            IconCached()
                        }
                        if (account.isAdministrator()) {
                            IconButton(onClick = { notification.notification() }) {
                                IconChat()
                            }
                        }
                    },
                    bodyContent = {
                        DepositScreen(
                            deposit = deposit,
                            cashbox = cashbox,
                            totalDebts = totalDebts,
                            contextDebts = contextDebts,
                            nextPageDebts = {
                                loading(contextDebts, DebtVO::event) { o, n ->
                                    debts.getByPersonAll(pk, o, n)
                                }
                            },
                            totalTransactions = totalTransactions,
                            contextTransactions = contextTransactions,
                            nextPageTransactions = {
                                loading(contextTransactions) { o, n ->
                                    transactions.getByPersonAll(pk, o, n)
                                }
                            },
                            navigateTo = ::navigateTo
                        )
                    },
                    navigateTo = ::navigateTo
                )
            }
        }
    }
}

enum class DepositTab(private val res: Int) {
    GENERAL(R.string.deposit_tab_general),
    DEBT(R.string.deposit_tab_debt),
    TRANSACTION(R.string.deposit_tab_transaction);

    @Composable
    val label: String
        get() = stringResource(res)
}

@Composable
fun DepositScreen(
    deposit: MutableState<DepositVO> = mutableStateOf(defaultDeposit),
    cashbox: MutableState<CashboxVO> = mutableStateOf(defaultCashbox),
    totalDebts: MutableState<Long>,
    contextDebts: MutableState<List<EventVO>>,
    nextPageDebts: () -> Unit,
    totalTransactions: MutableState<Long>,
    contextTransactions: MutableState<List<TransactionVO>>,
    nextPageTransactions: () -> Unit,
    navigateTo: (Screen) -> Unit
) {
    val selected = remember { mutableStateOf(DepositTab.GENERAL) }

    Column {
        TabRow(
            backgroundColor = MaterialTheme.colors.surface,
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

        when (selected.value) {
            DepositTab.GENERAL -> DepositView(
                deposit = deposit.value,
                cashbox = cashbox.value
            )
            DepositTab.DEBT -> EventPageView(
                list = contextDebts.value,
                total = totalDebts.value,
                navigateTo = navigateTo,
                navigateToNextPageScreen = nextPageDebts
            )
            DepositTab.TRANSACTION -> TransactionPageView(
                list = contextTransactions.value,
                total = totalTransactions.value,
                navigateTo = navigateTo,
                navigateToNextPageScreen = nextPageTransactions
            )
        }
    }
}
