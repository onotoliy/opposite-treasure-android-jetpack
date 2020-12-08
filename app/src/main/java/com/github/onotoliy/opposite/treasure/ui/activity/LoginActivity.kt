package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
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
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.components.TextField
import com.github.onotoliy.opposite.treasure.utils.*
import com.google.firebase.iid.FirebaseInstanceId
import java.io.IOException
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject lateinit var manager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

//        if (!manager.getAccountsByType(ACCOUNT_TYPE).isNullOrEmpty()) {
            navigateTo(Screen.TransactionPageScreen)
//        }
//
//        setContent {
//            TreasureTheme {
//                LoginScreen { account, password, token ->
//                    manager.addAccount(account, password, token)
//
//                    navigateTo(Screen.DepositScreen(manager.getUUID()))
//                }
//            }
//        }

        getToken()
    }

    private fun getToken() {
        Thread {
            try {
                val newToken = FirebaseInstanceId.getInstance().getToken("827738396697", "FCM")

                println("Token --> $newToken")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    companion object {
        private const val EXTRA_ACCOUNT_TYPE = "EXTRA_ACCOUNT_TYPE"
        private const val EXTRA_AUTH_TOKEN_TYPE = "EXTRA_AUTH_TOKEN_TYPE"
        private const val EXTRA_IS_NEW_ACCOUNT = "EXTRA_IS_NEW_ACCOUNT"

        fun getIntent(
            context: Context,
            response: AccountAuthenticatorResponse?,
            accountType: String?,
            authTokenType: String?,
            isNewAccount: Boolean
        ) = Intent(context, LoginActivity::class.java).apply {
            putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
            putExtra(EXTRA_ACCOUNT_TYPE, accountType)
            putExtra(EXTRA_AUTH_TOKEN_TYPE, authTokenType)
            putExtra(EXTRA_IS_NEW_ACCOUNT, isNewAccount)
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
                getAuthToken(
                    username = login.value,
                    password = password.value,
                    onResponse = {
                        it?.access_token?.let { token ->
                            addAccount(login.value, password.value, token)
                        }
                    },
                    onFailure = { }
                )
            }
        ) {
            Text(text = stringResource(id = R.string.auth_enter))
        }
    }
}