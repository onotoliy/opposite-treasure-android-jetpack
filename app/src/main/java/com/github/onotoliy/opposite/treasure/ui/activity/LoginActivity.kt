package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.goto
import com.github.onotoliy.opposite.treasure.resources.DefaultCallback
import com.github.onotoliy.opposite.treasure.services.AccessToken
import com.github.onotoliy.opposite.treasure.services.addAccount
import com.github.onotoliy.opposite.treasure.services.getUUID
import com.github.onotoliy.opposite.treasure.services.token
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.components.TextField

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manager: AccountManager = AccountManager.get(applicationContext)

        setContent {
            TreasureTheme {
                LoginScreen { account, password, token ->
                    manager.addAccount(account, password, token)

                    goto(Screen.DepositScreen(manager.getUUID()))
                }
            }
        }
    }

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