package com.github.onotoliy.opposite.treasure.di.restful.resource

import android.accounts.AccountManager
import android.util.Log
import com.github.onotoliy.opposite.treasure.di.restful.retrofit.UserRetrofit
import com.github.onotoliy.opposite.treasure.utils.getAuthToken
import java.util.concurrent.Executors
import javax.inject.Inject

class NotificationResource @Inject constructor(
    private val retrofit: UserRetrofit,
    private val account: AccountManager
) {
    fun notification() {
        Executors.newSingleThreadExecutor().execute {
            val r: retrofit2.Response<Void> = retrofit.notification(account.getAuthToken()).execute()

            Log.i("NotificationResource", "Code: ${r.code()}. Message: ${r.message()}")
        }
    }
}
