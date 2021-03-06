package com.github.onotoliy.opposite.treasure.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AuthenticatorService : Service() {

    private lateinit var authenticator: AccountAuthenticator

    override fun onCreate() {
        super.onCreate()

        authenticator = AccountAuthenticator(this)
    }

    override fun onBind(intent: Intent?): IBinder = authenticator.iBinder
}
