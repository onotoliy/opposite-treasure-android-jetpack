package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.github.onotoliy.opposite.treasure.*
import com.github.onotoliy.opposite.treasure.di.service.DepositService
import com.github.onotoliy.opposite.treasure.di.model.EventActivityModel
import com.github.onotoliy.opposite.treasure.di.service.DebtorService
import com.github.onotoliy.opposite.treasure.di.service.EventService
import com.github.onotoliy.opposite.treasure.di.service.TransactionService
import com.github.onotoliy.opposite.treasure.ui.IconEdit
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.views.DepositPageView
import com.github.onotoliy.opposite.treasure.ui.views.EventView
import com.github.onotoliy.opposite.treasure.ui.views.TransactionPageView
import javax.inject.Inject

class EventActivity : AppCompatActivity()  {

    @Inject
    lateinit var eventService: EventService

    @Inject
    lateinit var debtorService: DebtorService

    @Inject
    lateinit var depositService: DepositService

    @Inject
    lateinit var transactionService: TransactionService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).appComponent.inject(this)

        val pk = intent?.getStringExtra("pk") ?: ""
        val navigateTo: (Screen) -> Unit = { goto(it) }
        val screen = EventActivityModel(
            pk = pk,
            debtorService = debtorService,
            eventService = eventService,
            transactionService = transactionService
        )

        screen.loading()

        setContent {
            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            icon = { IconEdit() },
                            onClick = { }
                        )
                    },
                    bodyContent = { EventScreen(screen, navigateTo) },
                    navigateTo = navigateTo
                )
            }
        }
    }

}

@Composable
fun EventScreen(model: EventActivityModel, navigateTo: (Screen) -> Unit) {
    val selected = remember { mutableStateOf(EventTab.GENERAL) }

    model.pending.observe()?.let { pending ->
        Column {
            TabRow(
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

            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            when (selected.value) {
                EventTab.GENERAL -> model.event.observe()?.let {
                    EventView(data = it, navigateTo = navigateTo)
                }
                EventTab.DEBTORS -> model.debtors.observe()?.let {
                    DepositPageView(
                        view = it,
                        navigateTo = navigateTo,
                        navigateToNextPageScreen = { offset, numberOrRows, _ ->
                            model.nextDepositPageLoading(offset, numberOrRows)
                        }
                    )
                }
                EventTab.TRANSACTIONS -> model.transactions.observe()?.let {
                    TransactionPageView(
                        view = it,
                        navigateTo = navigateTo,
                        navigateToNextPageScreen = { offset, numberOrRows, _ ->
                            model.nextTransactionPageLoading(offset, numberOrRows)
                        }
                    )
                }
            }
        }
    }
}