package com.github.onotoliy.opposite.treasure.ui.activity

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.onotoliy.opposite.treasure.R
import com.github.onotoliy.opposite.treasure.Screen
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.components.TextField
import com.github.onotoliy.opposite.treasure.utils.ACCOUNT_TYPE
import com.github.onotoliy.opposite.treasure.utils.addAccount
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import com.github.onotoliy.opposite.treasure.utils.inject
import com.github.onotoliy.opposite.treasure.utils.navigateTo
import com.github.onotoliy.opposite.treasure.utils.setDefaultUncaughtExceptionHandler
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import java.io.IOException
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var account: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()

        setDefaultUncaughtExceptionHandler()

        if (!account.getAccountsByType(ACCOUNT_TYPE).isNullOrEmpty()) {
            navigateTo(Screen.LoadingScreen)
        }

        setContent {
            TreasureTheme {
                LoginScreen { account, password, token ->
                    this.account.addAccount(account, password, token)

                    navigateTo(Screen.LoadingScreen)
                }
            }
        }

        getToken()
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().subscribeToTopic("channel_id")

        Thread {
            try {
                FirebaseInstanceId.getInstance().getToken("827738396697", "FCM")
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
    val login = remember("") { mutableStateOf("") }
    val password = remember("") { mutableStateOf("") }
    val exceptions = remember(false) { mutableStateOf(false) }
    val message = remember("false") { mutableStateOf("false") }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            label = stringResource(id = R.string.auth_login),
            value = login.value,
            modifier = Modifier.fillMaxWidth(0.8f),
            onValueChange = { login.value = it },
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.padding(10.dp))

        TextField(
            label = stringResource(id = R.string.auth_password),
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(0.8f),
            onValueChange = { password.value = it },
            textAlign = TextAlign.Center
        )

        if (exceptions.value) {
            Spacer(Modifier.padding(10.dp))

            Text(text = message.value, modifier = Modifier.padding(10.dp), color = Color.Red)
            Text(text = stringResource(R.string.loading_exceptions))
        }

        Spacer(Modifier.padding(10.dp))

        Button(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = {
                getAuthToken(
                    username = login.value,
                    password = password.value,
                    onResponse = {
                        if (it?.accessToken == null) {
                            exceptions.value = true
                            message.value = "Access token is null"
                        } else {
                            try {
                                addAccount(login.value, password.value, it.accessToken)
                            } catch (it: Exception) {
                                message.value = it.message ?: it.localizedMessage ?: it.stackTraceToString()
                            }
                        }
                    },
                    onFailure = {
                        exceptions.value = true
                        message.value = it.message ?: it.localizedMessage ?: it.stackTraceToString()
                    }
                )
            }
        ) {
            Text(text = stringResource(id = R.string.auth_enter))
        }
    }
}
