package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.TextField
import androidx.ui.foundation.TextFieldValue
import androidx.ui.foundation.drawBorder
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.input.KeyboardType
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.LinearProgressIndicator
import androidx.ui.res.stringResource
import androidx.ui.res.vectorResource
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.TextUnit
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.observe
import com.github.onotoliy.opposite.treasure.ui.H6_BOLD
import com.github.onotoliy.opposite.treasure.ui.Selector

val persons = listOf(
    Option("1", "1"),
    Option("2", "2"),
    Option("3", "3"),
    Option("4", "4"),
    Option("5", "5"))

@Composable
fun TransactionEditScreen(model: Screen.TransactionEditScreen) {
    val isOpenType: MutableState<Boolean> = state { false }
    val type: MutableState<Option> = state { TransactionType.CONTRIBUTION.let { Option(it.name, it.label) } }
    val isOpenPerson: MutableState<Boolean> = state { false }
    val person: MutableState<Option> = state { persons[0] }
    val isOpenEvent: MutableState<Boolean> = state { false }
    val event: MutableState<Option> = state { persons[0] }
    val cash = state { TextFieldValue("0.0") }
    val close: () -> Unit = {
        isOpenType.value = false
        isOpenPerson.value = false
        isOpenEvent.value = false
    }

    model.pending.observe()?.let { pending ->
        Column(modifier = Modifier.padding(5.dp)) {
            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Selector(
                title = stringResource(id = R.string.transaction_type),
                selected = type,
                isOpen = isOpenType,
                close = close,
                list = TransactionType.values().map { Option(it.name, it.label) }
            )

            Selector(
                title = stringResource(id = R.string.transaction_person),
                selected = person,
                isOpen = isOpenPerson,
                close = close,
                list = persons,
                asset = vectorResource(id = R.drawable.ic_account_circle)
            )

            Selector(
                title = stringResource(id = R.string.transaction_event),
                selected = event,
                isOpen = isOpenEvent,
                close = close,
                list = persons,
                asset = vectorResource(id = R.drawable.ic_event)
            )

            Text(text = stringResource(id = R.string.transaction_cash), style = H6_BOLD)
            TextField(
                value = cash.value,
                onValueChange = { cash.value = it },
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth().drawBorder(
                    1.dp,
                    Color.LightGray,
                    RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
                ),
                textStyle = TextStyle(
                    lineHeight = TextUnit.Em(5),
                    fontSize = TextUnit.Em(5),
                    textAlign = TextAlign.Left,
                    color = Color.DarkGray
                )
            )
        }
    }
}