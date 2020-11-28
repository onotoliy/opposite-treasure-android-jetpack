package com.github.onotoliy.opposite.treasure.utils

import android.accounts.AccountManager
import com.github.onotoliy.opposite.treasure.di.resource.KeycloakResource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getAuthToken(
    username: String,
    password: String,
    onResponse: (AccessToken?) -> Unit,
    onFailure: (Throwable) -> Unit = { }
) = keycloak
    .auth(username = username, password = password)
    .enqueue(
        object : Callback<AccessToken> {
            override fun onResponse(
                call: Call<AccessToken>,
                response: Response<AccessToken>
            ) = onResponse(response.body())

            override fun onFailure(
                call: Call<AccessToken>,
                t: Throwable
            ) = onFailure(t)
        }
    )

fun AccountManager.getAuthToken(): String? {
    val account = getAccount()
    val username = account.name
    val password = getPassword(account)

    return keycloak.auth(username = username, password = password).execute().body()?.access_token
}

private val keycloak: KeycloakResource
    get() = retrofit.create(KeycloakResource::class.java)

private val retrofit: Retrofit
    get() = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("http://185.12.95.242/")
        .build()

private val gson: Gson = GsonBuilder().setLenient().create()

data class AccessToken(
    val access_token: String?,
    val token_type: String?,
    val expires_in: Int?,
    val refresh_token: String?,
    val scope: String?,
    val client_id: String?,
    val client_secret: String?
)
