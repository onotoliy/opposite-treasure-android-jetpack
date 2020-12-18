package com.github.onotoliy.opposite.treasure.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.di.database.data.OptionVO
import com.github.onotoliy.opposite.treasure.di.model.TransactionEditActivityModel
import com.github.onotoliy.opposite.treasure.ui.IconSave
import com.github.onotoliy.opposite.treasure.ui.Menu
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.components.AutocompleteField
import com.github.onotoliy.opposite.treasure.ui.components.SelectionField
import com.github.onotoliy.opposite.treasure.ui.components.TextField
import com.github.onotoliy.opposite.treasure.ui.components.TextStyleLeft
import com.github.onotoliy.opposite.treasure.ui.components.calendar.CalendarField
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.observe
import com.github.onotoliy.opposite.treasure.utils.pk
import javax.inject.Inject

class TransactionEditActivity : AppCompatActivity()  {

    @Inject lateinit var model: TransactionEditActivityModel

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
                                navigateTo(Screen.TransactionPageScreen)
                            }
                        )
                    },
                    bodyContent = { TransactionEditScreen(model) },
                    navigateTo = ::navigateTo
                )
            }
        }
    }
}

@Composable
fun TransactionEditScreen(model: TransactionEditActivityModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SelectionField(
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 5.dp),
            value = model.type.observe() ?: OptionVO(),
            label = stringResource(id = R.string.transaction_edit_type),
            onValueChange = { model.type.postValue(it) },
            textStyle = TextStyleLeft,
            list = TransactionType.values().map { OptionVO(it.name, it.label) }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 5.dp) ,
            value = model.name.observe() ?: "",
            label = stringResource(id = R.string.transaction_edit_name),
            onValueChange = { model.name.postValue(it) },
            keyboardType = KeyboardType.Text,
            textStyle = TextStyleLeft
        )
        TextField(
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 5.dp),
            value = model.cash.observe() ?: "",
            label = stringResource(id = R.string.transaction_edit_cash),
            onValueChange = { model.cash.postValue(it) },
            keyboardType = KeyboardType.Number,
            textStyle = TextStyleLeft
        )
        AutocompleteField(
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 5.dp),
            value = model.person.observe() ?: OptionVO(),
            label = stringResource(id = R.string.transaction_edit_person),
            onValueChange = { model.person.postValue(it) },
            textStyle = TextStyleLeft,
            onSearchValue = {
                model.getPersons(it)
            }
        )
        AutocompleteField(
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 5.dp),
            value = model.event.observe() ?: OptionVO(),
            label = stringResource(id = R.string.transaction_edit_event),
            onValueChange = { model.event.postValue(it) },
            textStyle = TextStyleLeft,
            onSearchValue = { model.getEvents(it) }
        )
        CalendarField(
            modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 5.dp),
            value = model.transactionDate.observe() ?: "",
            label = stringResource(id = R.string.transaction_edit_date),
            onValueChange = { model.transactionDate.postValue(it) },
        )
    }
}