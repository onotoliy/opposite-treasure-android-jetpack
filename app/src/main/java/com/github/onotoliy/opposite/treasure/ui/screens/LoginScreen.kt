package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.TextField
import androidx.ui.foundation.TextFieldValue
import androidx.ui.foundation.drawBorder
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.input.KeyboardType
import androidx.ui.input.PasswordVisualTransformation
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.Button
import androidx.ui.res.stringResource
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontStyle
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.TextUnit
import androidx.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.services.token
import com.github.onotoliy.opposite.treasure.ui.typography

@Composable
fun LoginScreen(
    addAccount: (username: String, password: String, token: String) -> Unit
) {
    val login = state(init = { TextFieldValue("") })
    val password = state(init = { TextFieldValue("") })

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalGravity = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.auth_login),
            style = typography.subtitle1,
            color = Color.DarkGray
        )

        TextField(
            modifier = Modifier.drawBorder(
                1.dp,
                Color.LightGray,
                RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
            ),
            textStyle = TextStyle(
                lineHeight = TextUnit.Companion.Em(5),
                fontSize = TextUnit.Companion.Em(5),
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            ),
            value = login.value,
            onValueChange = {
                login.value = it
            },
            keyboardType = KeyboardType.Text
        )

        Text(
            text = stringResource(id = R.string.auth_password),
            style = typography.subtitle1,
            color = Color.DarkGray
        )
        TextField(
            modifier = Modifier.drawBorder(
                1.dp,
                Color.LightGray,
                RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)
            ),
            textStyle = TextStyle(
                lineHeight = TextUnit.Companion.Em(5),
                fontSize = TextUnit.Companion.Em(5),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Normal,
                color = Color.DarkGray
            ),
            value = password.value,
            onValueChange = {
                password.value = it
            },
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            modifier = Modifier.padding(5.dp),
            onClick = {
                token(login.value.text, password.value.text)?.let {
                    addAccount(login.value.text, password.value.text, it)
                }
            }
        ) {
            Text(text = stringResource(id = R.string.auth_enter))
        }
    }
}