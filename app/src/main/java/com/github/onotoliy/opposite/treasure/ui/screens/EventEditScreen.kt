package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
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
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.TextUnit
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.observe

@Composable
fun EventEditScreen(model: Screen.EventEditScreen) {
    model.pending.observe()?.let { pending ->
        Column(modifier = Modifier.padding(5.dp)) {
            if (pending) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            model.event.observe()?.let { event ->
                val name = state(init = { TextFieldValue(event.name) })
                val contribution = state(init = { TextFieldValue(event.contribution) })
                val total = state(init = { TextFieldValue(event.total) })
                val deadline = state(init = { TextFieldValue(event.deadline) })

                Text(text = stringResource(id = R.string.event_edit_name))
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
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

                Text(text = stringResource(id = R.string.event_edit_contribution))
                TextField(
                    value = contribution.value,
                    onValueChange = { contribution.value = it },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth().drawBorder(
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

                Text(text = stringResource(id = R.string.event_edit_total))
                TextField(
                    value = total.value,
                    onValueChange = { total.value = it },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth().drawBorder(
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

                Text(text = stringResource(id = R.string.event_edit_deadline))
                TextField(
                    value = deadline.value,
                    onValueChange = { deadline.value = it },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth().drawBorder(
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
    }
}