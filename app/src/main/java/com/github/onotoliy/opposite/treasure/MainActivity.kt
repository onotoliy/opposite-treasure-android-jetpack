package com.github.onotoliy.opposite.treasure

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.github.onotoliy.opposite.treasure.services.getUUID
import com.github.onotoliy.opposite.treasure.works.DepositWorker
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

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        val manager = AccountManager.get(applicationContext)

        val depositWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<DepositWorker>()
                .build()
        WorkManager
            .getInstance(applicationContext)
            .enqueue(listOf(depositWorkRequest))

        if (manager.getAccountsByType(ACCOUNT_TYPE).isNullOrEmpty()) {
            goto(Screen.LoginScreen)
        } else {
            goto(Screen.DepositScreen(manager.getUUID()))
        }

        getToken()
    }

    private fun getToken() {

        Thread {
            try {

                val newToken = FirebaseInstanceId.getInstance()
                    .getToken("827738396697", "FCM")
                println("Token --> $newToken")

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }
}
