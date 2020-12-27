package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.dao.DebtDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.DepositDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.TransactionDAO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import com.github.onotoliy.opposite.treasure.di.database.data.TransactionVO
import com.github.onotoliy.opposite.treasure.di.database.data.fromTransactionType
import com.github.onotoliy.opposite.treasure.di.database.data.toTransactionType
import com.github.onotoliy.opposite.treasure.ui.IconSave
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.components.AutocompleteField
import com.github.onotoliy.opposite.treasure.ui.components.MoneyVisualTransformation
import com.github.onotoliy.opposite.treasure.ui.components.SelectionField
import com.github.onotoliy.opposite.treasure.ui.components.TextField
import com.github.onotoliy.opposite.treasure.ui.components.calendar.CalendarField
import com.github.onotoliy.opposite.treasure.utils.*
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject

class TransactionEditActivity : AppCompatActivity() {

    @Inject
    lateinit var manager: AccountManager

    @Inject
    lateinit var debts: DebtDAO

    @Inject
    lateinit var events: EventDAO

    @Inject
    lateinit var deposits: DepositDAO

    @Inject
    lateinit var transactions: TransactionDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        setContent {
            val context = mutableStateOf(defaultTransaction) {
                intent.pk?.let(transactions::get) ?: MutableLiveData(
                    TransactionVO(
                        uuid = randomUUID(),
                        name = "",
                        creationDate = Date().toISO(),
                        deletionDate = null,
                        author = OptionVO(manager.getUUID(), manager.getName()),
                        local = 1,
                        milliseconds = milliseconds(),
                        cash = "0.0",
                        transactionDate = Date().toISO(),
                        event = null,
                        person = null,
                        type = TransactionType.NONE
                    )
                )
            }
            val events = mutableStateOf(defaultOptions, { OptionVO(it.uuid, it.name) }) { _, _ ->
                events.getAll(0, 20)
            }
            val persons = mutableStateOf(defaultOptions, { OptionVO(it.uuid, it.name) }) { _, _ ->
                deposits.getAll(0, 20)
            }

            TreasureTheme {
                Menu(
                    screen = Screen.TransactionEditScreen(intent.pk),
                    floatingActionButton = {
                        FloatingActionButton(
                            modifier = Modifier.border(1.dp, Color.LightGray, CircleShape),
                            backgroundColor = Color.White,
                            content = { IconSave() },
                            onClick = {
                                replace(context.value)
                                navigateTo(Screen.TransactionPageScreen)
                            }
                        )
                    },
                    bodyContent = {
                        TransactionEditScreen(
                            editable = intent.pk == null,
                            context = context,
                            events = events,
                            persons = persons,
                            qEvents = {
                                qEvents(
                                    q = it,
                                    person = context.value.person?.uuid,
                                    type = context.value.type,
                                    events = events
                                )
                            },
                            qPersons = { qPersons(it, persons) }
                        )
                    },
                    navigateTo = ::navigateTo
                )
            }
        }
    }

    private fun replace(context: TransactionVO) {
        Executors.newSingleThreadExecutor().execute {
            transactions.replace(context)
        }
    }

    private fun qPersons(
        q: String?,
        persons: MutableState<List<OptionVO>>
    ) {
        if (q == null) {
            deposits.getAll().observeForever { list ->
                persons.value = list.map { OptionVO(it.uuid, it.name) }
            }
        } else {
            deposits.getAll(q).observeForever { list ->
                persons.value = list.map { OptionVO(it.uuid, it.name) }
            }
        }
    }

    private fun qEvents(
        q: String?,
        person: String?,
        type: TransactionType,
        events: MutableState<List<OptionVO>>
    ) {
        if (type in listOf(TransactionType.CONTRIBUTION, TransactionType.WRITE_OFF)) {
            loadingOnlyEvents(q, events)
        } else {
            if (person.isNullOrEmpty()) {
                loadingOnlyEvents(q, events)
            } else {
                if (q.isNullOrEmpty()) {
                    this.debts.getByPersonAll(person).observeForever { list ->
                        events.value = (list.map { OptionVO(it.event.uuid, it.event.name) })
                    }
                } else {
                    this.debts.getByPersonAll(person, q).observeForever { list ->
                        events.value = (list.map { OptionVO(it.event.uuid, it.event.name) })
                    }
                }
            }
        }
    }

    private fun loadingOnlyEvents(q: String?, events: MutableState<List<OptionVO>>) {
        if (q.isNullOrEmpty()) {
            this.events.getAll().observeForever { list ->
                events.value = (list.map { OptionVO(it.uuid, it.name) })
            }
        } else {
            this.events.getAll(q).observeForever { list ->
                events.value = (list.map { OptionVO(it.uuid, it.name) })
            }
        }
    }
}

@Composable
fun TransactionEditScreen(
    editable: Boolean,
    context: MutableState<TransactionVO> = mutableStateOf(defaultTransaction),
    events:  MutableState<List<OptionVO>> = mutableStateOf(defaultOptions),
    persons:  MutableState<List<OptionVO>> = mutableStateOf(defaultOptions),
    qEvents: (String?) -> Unit,
    qPersons: (String?) -> Unit
) {
    val modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 5.dp)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SelectionField(
            modifier = modifier,
            value = context.value.type.fromTransactionType(),
            label = stringResource(id = R.string.transaction_edit_type),
            onValueChange = { context.value = context.value.copy(type = it.toTransactionType()) },
            list = TransactionType.values().map(TransactionType::fromTransactionType)
        )
        TextField(
            editable = editable,
            valueColor = if (editable) Color.Black else Color.Gray,
            modifier = modifier,
            value = context.value.name,
            label = stringResource(id = R.string.transaction_edit_name),
            onValueChange = { context.value = context.value.copy(name = it) },
            keyboardType = KeyboardType.Text
        )
        TextField(
            editable = editable,
            valueColor = if (editable) Color.Black else Color.Gray,
            modifier = modifier,
            value = context.value.cash,
            label = stringResource(id = R.string.transaction_edit_cash),
            onValueChange = { context.value = context.value.copy(cash = it) },
            keyboardType = KeyboardType.Number,
            visualTransformation = MoneyVisualTransformation()
        )
        AutocompleteField(
            modifier = modifier,
            value = context.value.person ?: OptionVO(),
            list = persons.value,
            label = stringResource(id = R.string.transaction_edit_person),
            onValueChange = { context.value = context.value.copy(person = it) },
            onSearchValue = qPersons
        )
        AutocompleteField(
            modifier = modifier,
            value = context.value.event ?: OptionVO(),
            list = events.value,
            label = stringResource(id = R.string.transaction_edit_event),
            onValueChange = { context.value = context.value.copy(event = it) },
            onSearchValue = qEvents
        )
        CalendarField(
            editable = editable,
            valueColor = if (editable) Color.Black else Color.Gray,
            modifier = modifier,
            value = context.value.transactionDate.fromISO().toShortDate(),
            label = stringResource(id = R.string.transaction_edit_date),
            onValueChange = { context.value = context.value.copy(transactionDate = it) },
            divider = { Divider(color = Color.Gray) }
        )
    }
}
