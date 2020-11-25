package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.runtime.Composable
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.treasure.Screen

val persons = listOf(
    Option("1", "1"),
    Option("2", "2"),
    Option("3", "3"),
    Option("4", "4"),
    Option("5", "5"))

@Composable
fun TransactionEditScreen(model: Screen.TransactionEditScreen) {/*
    val isOpenType: MutableState<Boolean> = remember { mutableStateOf(false) }
    val type: MutableState<Option> = remember { mutableStateOf(TransactionType.CONTRIBUTION.let { Option(it.name, it.label) }) }
    val isOpenPerson: MutableState<Boolean> = remember { mutableStateOf(false) }
    val person: MutableState<Option> = remember { mutableStateOf(persons[0]) }
    val isOpenEvent: MutableState<Boolean> = remember { mutableStateOf(false) }
    val event: MutableState<Option> = remember { mutableStateOf(persons[0]) }
    val cash = remember { mutableStateOf(TextFieldValue("0.0")) }
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
                modifier = Modifier.fillMaxWidth().border(
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
    }*/
}