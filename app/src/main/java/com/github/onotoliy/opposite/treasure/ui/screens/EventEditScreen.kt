package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.foundation.Text
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.observe
import com.github.onotoliy.opposite.treasure.ui.H6_BOLD

@Composable
fun EventEditScreen(model: Screen.EventEditScreen) {/*
    model.pending.observe()?.let { pending ->
        Column(modifier = Modifier.padding(5.dp)) {
            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            model.event.observe()?.let { event ->
                val name = remember { mutableStateOf(TextFieldValue(event.name)) }
                val contribution = remember { mutableStateOf( TextFieldValue(event.contribution)) }
                val total = remember { mutableStateOf( TextFieldValue(event.total)) }
                val deadline = remember { mutableStateOf( TextFieldValue(event.deadline)) }

                Text(text = stringResource(id = R.string.event_edit_name), style = H6_BOLD)
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
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

                Text(text = stringResource(id = R.string.event_edit_contribution), style = H6_BOLD)
                TextField(
                    value = contribution.value,
                    onValueChange = { contribution.value = it },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth().border(
                        1.dp,
                        Color.LightGray,
                        RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
                    ),
                    textStyle = TextStyle(
                        lineHeight = TextUnit.Companion.Em(5),
                        fontSize = TextUnit.Companion.Em(5),
                        textAlign = TextAlign.Left,
                        color = Color.DarkGray
                    )
                )

                Text(text = stringResource(id = R.string.event_edit_total), style = H6_BOLD)
                TextField(
                    value = total.value,
                    onValueChange = { total.value = it },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth().border(
                        1.dp,
                        Color.LightGray,
                        RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
                    ),
                    textStyle = TextStyle(
                        lineHeight = TextUnit.Companion.Em(5),
                        fontSize = TextUnit.Companion.Em(5),
                        textAlign = TextAlign.Left,
                        color = Color.DarkGray
                    )
                )

                Text(text = stringResource(id = R.string.event_edit_deadline), style = H6_BOLD)
                TextField(
                    value = deadline.value,
                    onValueChange = { deadline.value = it },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth().border(
                        1.dp,
                        Color.LightGray,
                        RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
                    ),
                    textStyle = TextStyle(
                        lineHeight = TextUnit.Companion.Em(5),
                        fontSize = TextUnit.Companion.Em(5),
                        textAlign = TextAlign.Left,
                        color = Color.DarkGray
                    )
                )
            }
        }
    }*/
}