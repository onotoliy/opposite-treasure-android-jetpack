package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.EventVO
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import com.github.onotoliy.opposite.treasure.di.database.repositories.EventRepository
import com.github.onotoliy.opposite.treasure.ui.IconSave
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.components.MoneyVisualTransformation
import com.github.onotoliy.opposite.treasure.ui.components.TextField
import com.github.onotoliy.opposite.treasure.ui.components.calendar.CalendarField
import com.github.onotoliy.opposite.treasure.utils.GLOBAL
import com.github.onotoliy.opposite.treasure.utils.INSERT
import com.github.onotoliy.opposite.treasure.utils.UPDATE
import com.github.onotoliy.opposite.treasure.utils.defaultEvent
import com.github.onotoliy.opposite.treasure.utils.fromISO
import com.github.onotoliy.opposite.treasure.utils.getName
import com.github.onotoliy.opposite.treasure.utils.getUUID
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.milliseconds
import com.github.onotoliy.opposite.treasure.utils.mutableStateOf
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.pk
import com.github.onotoliy.opposite.treasure.utils.toISO
import com.github.onotoliy.opposite.treasure.utils.toShortDate
import com.github.onotoliy.opposite.treasure.utils.uuid
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject

class EventEditActivity : AppCompatActivity() {

    @Inject
    lateinit var manager: AccountManager

    @Inject
    lateinit var dao: EventRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        setContent {
            val context = mutableStateOf(defaultEvent) {
                intent.pk?.let(dao::get) ?: MutableLiveData(
                    EventVO(
                        uuid = uuid(),
                        total = "0.0",
                        contribution = "0.0",
                        name = "",
                        deadline = Date().toISO(),
                        creationDate = Date().toISO(),
                        deletionDate = null,
                        author = OptionVO(manager.getUUID(), manager.getName()),
                        local = INSERT,
                        milliseconds = milliseconds()
                    )
                )
            }

            TreasureTheme {
                Menu(
                    screen = Screen.EventEditScreen(intent.pk),
                    floatingActionButton = {
                        FloatingActionButton(
                            modifier = Modifier.border(1.dp, Color.LightGray, CircleShape),
                            backgroundColor = Color.White,
                            content = { IconSave() },
                            onClick = { replace(context.value) }
                        )
                    },
                    bodyContent = { EventEditScreen(context.value.local == INSERT, context) },
                    navigateTo = ::navigateTo
                )
            }
        }
    }

    private fun replace(context: EventVO) {
        Executors.newSingleThreadExecutor().execute {
            if (context.local == GLOBAL) {
                context.local = UPDATE
            }

            dao.replace(context)

            navigateTo(Screen.EventPageScreen)
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
            modifier = Modifier.fillMaxWidth(0.8f),
            value = context.value.name,
            label = stringResource(id = R.string.event_edit_name),
            onValueChange = { context.value = context.value.copy(name = it) }
        )

        Spacer(Modifier.padding(5.dp))

        TextField(
            editable = editable,
            valueColor = if (editable) Color.Black else Color.Gray,
            modifier = Modifier.fillMaxWidth(0.8f),
            value = context.value.contribution,
            label = stringResource(id = R.string.event_edit_contribution),
            onValueChange = { context.value = context.value.copy(contribution = it) },
            keyboardType = KeyboardType.Number,
            visualTransformation = MoneyVisualTransformation()
        )

        Spacer(Modifier.padding(5.dp))

        TextField(
            editable = editable,
            valueColor = if (editable) Color.Black else Color.Gray,
            modifier = Modifier.fillMaxWidth(0.8f),
            value = context.value.total,
            label = stringResource(id = R.string.event_edit_total),
            onValueChange = { context.value = context.value.copy(total = it) },
            keyboardType = KeyboardType.Number,
            visualTransformation = MoneyVisualTransformation()
        )

        Spacer(Modifier.padding(5.dp))

        CalendarField(
            editable = editable,
            valueColor = if (editable) Color.Black else Color.Gray,
            modifier = Modifier.fillMaxWidth(0.8f),
            value = context.value.deadline.fromISO().toShortDate(),
            label = stringResource(id = R.string.event_edit_deadline),
            onValueChange = { context.value = context.value.copy(deadline = it) }
        )
    }
}
