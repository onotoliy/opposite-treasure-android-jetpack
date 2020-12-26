package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.dao.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.DepositVO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.model.EventActivityModel
import com.github.onotoliy.opposite.treasure.ui.IconEdit
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.DepositPageViewVO
import com.github.onotoliy.opposite.treasure.ui.views.EventView
import com.github.onotoliy.opposite.treasure.ui.views.TransactionPageViewVO
import com.github.onotoliy.opposite.treasure.utils.defaultDeposits
import com.github.onotoliy.opposite.treasure.utils.defaultEvent
import com.github.onotoliy.opposite.treasure.utils.defaultTransactions
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.loading
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.pk
import javax.inject.Inject

class EventActivity : AppCompatActivity() {

    @Inject
    lateinit var model: EventActivityModel

    @Inject
    lateinit var event: EventDAO

    @Inject
    lateinit var debtors: DebtDAO

    @Inject
    lateinit var transactions: TransactionDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pk = intent.pk ?: throw IllegalArgumentException("Primary key can not be null")

        inject()

        setContent {
            val event = mutableStateOf(defaultEvent) { event.get(pk) }
            val totalDebtors = mutableStateOf(0L) { debtors.countByEvent(pk) }
            val contextDebtors = mutableStateOf(
                default = defaultDeposits,
                loading = { o, n -> debtors.getByEventAll(pk, o, n) },
                convert = { it.deposit }
            )
            val totalTransactions = mutableStateOf(0L) { transactions.countByEvent(pk) }
            val contextTransactions = mutableStateOf(
                default = defaultTransactions,
                loading = { o, n -> transactions.getByEventAll(pk, o, n) },
            )

            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            content = { IconEdit() },
                            onClick = { navigateTo(Screen.EventEditScreen(pk)) }
                        )
                    },
                    bodyContent = {
                        EventScreen(
                            event = event,
                            totalDebtors = totalDebtors,
                            contextDebtors = contextDebtors,
                            nextPageDebtors = {
                                loading(contextDebtors, { it.deposit }) { o, n ->
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
                    text = { androidx.compose.material.Text(text = tab.label) }
                )
            }
        }

        when (selected.value) {
            EventTab.GENERAL -> EventView(
                data = event.value,
                navigateTo = navigateTo
            )
            EventTab.DEBTORS -> DepositPageViewVO(
                list = contextDebtors.value,
                total = totalDebtors.value,
                navigateTo = navigateTo,
                navigateToNextPageScreen = nextPageDebtors
            )
            EventTab.TRANSACTIONS -> TransactionPageViewVO(
                list = contextTransactions.value,
                total = totalTransactions.value,
                navigateTo = navigateTo,
                navigateToNextPageScreen = nextPageTransactions
            )
        }
    }
}
