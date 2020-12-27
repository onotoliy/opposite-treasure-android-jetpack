package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.dao.EventDAO
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import com.github.onotoliy.opposite.treasure.ui.IconSave
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.components.MoneyVisualTransformation
import com.github.onotoliy.opposite.treasure.ui.components.TextField
import com.github.onotoliy.opposite.treasure.ui.components.calendar.CalendarField
import com.github.onotoliy.opposite.treasure.utils.defaultEvent
import com.github.onotoliy.opposite.treasure.utils.fromISO
import com.github.onotoliy.opposite.treasure.utils.getName
import com.github.onotoliy.opposite.treasure.utils.getUUID
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.milliseconds
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.pk
import com.github.onotoliy.opposite.treasure.utils.randomUUID
import com.github.onotoliy.opposite.treasure.utils.toISO
import com.github.onotoliy.opposite.treasure.utils.toShortDate
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject

class EventEditActivity : AppCompatActivity() {

    @Inject
    lateinit var manager: AccountManager

    @Inject
    lateinit var dao: EventDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        setContent {
            val context = mutableStateOf(defaultEvent) {
                intent.pk?.let(dao::get) ?: MutableLiveData(
                    EventVO(
                        uuid = randomUUID(),
                        total = "0.0",
                        contribution = "0.0",
                        name = "",
                        deadline = Date().toISO(),
                        creationDate = Date().toISO(),
                        deletionDate = null,
                        author = OptionVO(manager.getUUID(), manager.getName()),
                        local = 1,
                        milliseconds = milliseconds()
                    )
                )
            }

            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            content = { IconSave() },
                            onClick = {
                                replace(context.value)
                                navigateTo(Screen.EventPageScreen)
                            }
                        )
                    },
                    bodyContent = { EventEditScreen(intent.pk == null, context) },
                    navigateTo = ::navigateTo
                )
            }
        }
    }

    private fun replace(context: EventVO) {
        Executors.newSingleThreadExecutor().execute {
            dao.replace(context)
        }
    }
}


@Composable
fun EventEditScreen(
    editable: Boolean,
    context: MutableState<EventVO> = mutableStateOf(defaultEvent)
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            editable = editable,
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 5.dp),
            value = context.value.name,
            label = stringResource(id = R.string.event_edit_name),
            onValueChange = { context.value.name = it }
        )
        TextField(
            editable = editable,
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 5.dp),
            value = context.value.contribution,
            label = stringResource(id = R.string.event_edit_contribution),
            onValueChange = { context.value.contribution = it },
            keyboardType = KeyboardType.Number,
            visualTransformation = MoneyVisualTransformation()
        )
        TextField(
            editable = editable,
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 5.dp),
            value = context.value.total,
            label = stringResource(id = R.string.event_edit_total),
            onValueChange = { context.value.total = it },
            keyboardType = KeyboardType.Number,
            visualTransformation = MoneyVisualTransformation()
        )
        CalendarField(
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 5.dp),
            value = context.value.deadline.fromISO().toShortDate(),
            label = stringResource(id = R.string.event_edit_deadline),
            onValueChange = { context.value.deadline }
        )
    }
}
