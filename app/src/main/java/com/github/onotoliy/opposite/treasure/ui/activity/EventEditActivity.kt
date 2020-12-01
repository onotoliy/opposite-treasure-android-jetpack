package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.model.EventEditActivityModel
import com.github.onotoliy.opposite.treasure.ui.IconSave
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.components.TextField
import com.github.onotoliy.opposite.treasure.ui.components.TextStyleLeft
import com.github.onotoliy.opposite.treasure.ui.components.calendar.CalendarField
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.observe
import com.github.onotoliy.opposite.treasure.utils.pk
import javax.inject.Inject

class EventEditActivity : AppCompatActivity()  {

    @Inject lateinit var model: EventEditActivityModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        model.loading(intent.pk ?: "")

        setContent {
            TreasureTheme {
                Menu(
                    floatingActionButton = {
                        FloatingActionButton(
                            icon = { IconSave() },
                            onClick = {
                                model.merge()
                                navigateTo(Screen.EventPageScreen)
                            }
                        )
                    },
                    bodyContent = { EventEditScreen(model) },
                    navigateTo = ::navigateTo
                )
            }
        }
    }
}


@Composable
fun EventEditScreen(model: EventEditActivityModel) {
    Column {
        TextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
            value = model.name.observe() ?: "",
            label = stringResource(id = R.string.event_edit_name),
            onValueChange = { model.name.postValue(it) },
            textStyle = TextStyleLeft
        )
        TextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp) ,
            value = model.contribution.observe() ?: "",
            label = stringResource(id = R.string.event_edit_contribution),
            onValueChange = { model.contribution.postValue(it) },
            keyboardType = KeyboardType.Number,
            textStyle = TextStyleLeft
        )
        TextField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
            value = model.total.observe() ?: "",
            label = stringResource(id = R.string.event_edit_total),
            onValueChange = { model.total.postValue(it) },
            keyboardType = KeyboardType.Number,
            textStyle = TextStyleLeft
        )
        CalendarField(
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
            value = model.deadline.observe() ?: "",
            label = stringResource(id = R.string.event_edit_deadline),
            onValueChange = { model.deadline.postValue(it) },
        )
    }
}