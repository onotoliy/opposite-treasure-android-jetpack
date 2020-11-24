package com.github.onotoliy.opposite.treasure.ui.screens

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.resources.DefaultCallback
import com.github.onotoliy.opposite.treasure.services.AccessToken
import com.github.onotoliy.opposite.treasure.services.token
import com.github.onotoliy.opposite.treasure.ui.components.AutocompleteFieldPreview
import com.github.onotoliy.opposite.treasure.ui.components.SelectionFieldPreview
import com.github.onotoliy.opposite.treasure.ui.components.TextField
import retrofit2.Callback

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(addAccount = { _, _, _ -> })
}

@Composable
fun LoginScreen(
    addAccount: (username: String, password: String, token: String) -> Unit
) {
    val login = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            label = stringResource(id = R.string.auth_login),
            value = login.value,
            modifier = Modifier.fillMaxWidth(0.8f).padding(0.dp, 15.dp)
        ) { login.value = it }

        TextField(
            label = stringResource(id = R.string.auth_password),
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) { password.value = it }

        Button(
            modifier = Modifier.fillMaxWidth(0.8f).padding(0.dp, 15.dp),
            onClick = {
                token(login.value, password.value, DefaultCallback<AccessToken>(
                    onResponse = {
                        it?.access_token?.let { token ->
                            addAccount(login.value, password.value, token)
                        }
                    },
                    onFailure = { }
                ))
            }
        ) {
            Text(text = stringResource(id = R.string.auth_enter))
        }
    }
}