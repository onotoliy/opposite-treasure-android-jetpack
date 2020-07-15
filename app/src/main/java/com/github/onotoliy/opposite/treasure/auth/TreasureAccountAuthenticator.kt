package com.github.onotoliy.opposite.treasure.auth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.github.onotoliy.opposite.treasure.MainActivity

class TreasureAccountAuthenticator(private val context: Context): AbstractAccountAuthenticator(context) {
    override fun getAuthTokenLabel(authTokenType: String?): String? = null
    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?, options: Bundle?): Bundle? = null
    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle? = null
    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle? = null
    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle? = null

    override fun getAuthToken(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle {
        val token = AccountManager.get(context).peekAuthToken(account, authTokenType)

        if (TextUtils.isEmpty(token)) {
            return Bundle().apply {
                putParcelable(
                    AccountManager.KEY_INTENT,
                    MainActivity.getIntent(
                        context,
                        response,
                        account?.type,
                        authTokenType,
                        false
                    )
                )
            }
        }

        return Bundle().apply {
            putString(AccountManager.KEY_ACCOUNT_NAME, account?.name)
            putString(AccountManager.KEY_ACCOUNT_TYPE, account?.type)
            putString(AccountManager.KEY_AUTHTOKEN, token)
        }

    }

    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle {
        return Bundle().apply {
            putParcelable(
                AccountManager.KEY_INTENT,
                MainActivity.getIntent(context, response, accountType, authTokenType, true)
            )
        }
    }
}