package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.DebtVO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.DebtRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.EventRepository
import com.github.onotoliy.opposite.treasure.di.database.repositories.TransactionRepository
import com.github.onotoliy.opposite.treasure.ui.IconEdit
import com.github.onotoliy.opposite.treasure.ui.IconRemove
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.DepositPageView
import com.github.onotoliy.opposite.treasure.ui.views.EventView
import com.github.onotoliy.opposite.treasure.ui.views.TransactionPageView
import com.github.onotoliy.opposite.treasure.utils.DELETE
import com.github.onotoliy.opposite.treasure.utils.INSERT
import com.github.onotoliy.opposite.treasure.utils.defaultDeposits
import com.github.onotoliy.opposite.treasure.utils.defaultEvent
import com.github.onotoliy.opposite.treasure.utils.defaultTransactions
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.isAdministrator
import com.github.onotoliy.opposite.treasure.utils.loading
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.pk
import com.github.onotoliy.opposite.treasure.utils.setDefaultUncaughtExceptionHandler
import java.util.concurrent.Executors
import javax.inject.Inject

class EventActivity : AppCompatActivity() {

    @Inject
    lateinit var account: AccountManager

    @Inject
    lateinit var event: EventRepository

    @Inject
    lateinit var debtors: DebtRepository

    @Inject
    lateinit var transactions: TransactionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pk = intent.pk ?: throw IllegalArgumentException("Primary key can not be null")

        inject()

        setDefaultUncaughtExceptionHandler()

        setContent {
            val event = mutableStateOf(defaultEvent) { event.get(pk) }
            val totalDebtors = mutableStateOf(0L) { debtors.countByEvent(pk) }
            val contextDebtors = mutableStateOf(defaultDeposits, DebtVO::deposit) { o, n ->
                debtors.getByEventAll(pk, o, n)
            }
            val totalTransactions = mutableStateOf(0L) { transactions.countByEvent(pk) }
            val contextTransactions = mutableStateOf(defaultTransactions) { o, n ->
                transactions.getByEventAll(pk, o, n)
            }

            TreasureTheme {
                Menu(
                    screen = Screen.EventScreen(pk),
                    actions = {
                        if (event.value.local == INSERT && account.isAdministrator()) {
                            IconButton(onClick = {
                                delete(event.value)
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
                                onClick = { navigateTo(Screen.EventEditScreen(pk)) }
                            )
                        }
                    },
                    bodyContent = {
                        EventScreen(
                            event = event,
                            totalDebtors = totalDebtors,
                            contextDebtors = contextDebtors,
                            nextPageDebtors = {
                                loading(contextDebtors, DebtVO::deposit) { o, n ->
                                    debtors.getByEventAll(pk, o, n)
                                }
                            },
                            totalTransactions = totalTransactions,
                            contextTransactions = contextTransactions,
                            nextPageTransactions = {
                                loading(contextTransactions) { o, n ->
                                    transactions.getByEventAll(pk, o, n)
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

    private fun delete(context: EventVO) {
        Executors.newSingleThreadExecutor().execute {
            context.local = DELETE

            event.replace(context)

            navigateTo(Screen.EventPageScreen)
        }
    }
}

enum class EventTab(private val res: Int) {
    GENERAL(R.string.event_tab_general),
    DEBTORS(R.string.event_tab_debtor),
    TRANSACTIONS(R.string.event_tab_transaction);

    @Composable
    val label: String
        get() = stringResource(res)
}

@Composable
fun EventScreen(
    event: MutableState<EventVO> = mutableStateOf(defaultEvent),
    totalDebtors: MutableState<Long>,
    contextDebtors: MutableState<List<DepositVO>>,
    nextPageDebtors: () -> Unit,
    totalTransactions: MutableState<Long>,
    contextTransactions: MutableState<List<TransactionVO>>,
    nextPageTransactions: () -> Unit,
    navigateTo: (Screen) -> Unit
) {
    val selected = remember { mutableStateOf(EventTab.GENERAL) }

    Column {
        TabRow(
            backgroundColor = MaterialTheme.colors.surface,
            selectedTabIndex = selected.value.ordinal
        ) {
            EventTab.values().forEach { tab ->
                Tab(
                    selected = tab == selected.value,
                    onClick = { selected.value = tab },
                    text = { Text(text = tab.label) }
                )
            }
        }

        when (selected.value) {
            EventTab.GENERAL -> EventView(
                dto = event.value,
                navigateTo = navigateTo
            )
            EventTab.DEBTORS -> DepositPageView(
                list = contextDebtors.value,
                total = totalDebtors.value,
                navigateTo = navigateTo,
                navigateToNextPageScreen = nextPageDebtors
            )
            EventTab.TRANSACTIONS -> TransactionPageView(
                list = contextTransactions.value,
                total = totalTransactions.value,
                navigateTo = navigateTo,
                navigateToNextPageScreen = nextPageTransactions
            )
        }
    }
}
