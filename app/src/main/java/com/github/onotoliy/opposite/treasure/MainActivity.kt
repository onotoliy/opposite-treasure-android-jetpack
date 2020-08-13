package com.github.onotoliy.opposite.treasure

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent
import com.github.onotoliy.opposite.treasure.ACCOUNT_TYPE
import com.github.onotoliy.opposite.treasure.ui.TreasureTheme
import com.github.onotoliy.opposite.treasure.ui.screens.TreasureScreen
import com.google.firebase.iid.FirebaseInstanceId
import java.io.IOException

class MainActivity : AppCompatActivity() {

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
        ) = Intent(context, MainActivity::class.java).apply {
            putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
            putExtra(EXTRA_ACCOUNT_TYPE, accountType)
            putExtra(EXTRA_AUTH_TOKEN_TYPE, authTokenType)
            putExtra(EXTRA_IS_NEW_ACCOUNT, isNewAccount)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manager: AccountManager = AccountManager.get(applicationContext)
        val firstScreen = if (manager.getAccountsByType(ACCOUNT_TYPE).isNullOrEmpty()) {
            Screen.LoginScreen
        } else {
            Screen.DepositScreen()
        }

        setContent {
            TreasureTheme {
                TreasureScreen(firstScreen, manager)
            }
        }

        getToken()
    }

    private fun getToken() {

        Thread(Runnable {
            try {

                val newToken = FirebaseInstanceId.getInstance()
                    .getToken("827738396697", "FCM")
                println("Token --> $newToken")

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }
}
