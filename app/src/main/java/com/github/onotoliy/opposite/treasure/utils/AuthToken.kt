package com.github.onotoliy.opposite.treasure.utils

import android.accounts.AccountManager
import com.github.onotoliy.opposite.treasure.di.restful.retrofit.KeycloakRetrofit
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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

fun AccountManager.getAuthToken() : String {
    val account = getAccount()
    val username = account.name
    val password = getPassword(account)
    val token = keycloak.auth(username = username, password = password).execute().body()?.accessToken
        ?: throw IllegalArgumentException()

    return "Bearer $token"
}

private val keycloak: KeycloakRetrofit
    get() = retrofit.create(KeycloakRetrofit::class.java)

private val retrofit: Retrofit
    get() = Retrofit
        .Builder()
        .client(OkHttpClient
            .Builder()
            .writeTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .connectionPool(ConnectionPool(15, 5, TimeUnit.MINUTES))
            .build()
        )
        .addConverterFactory(GsonConverterFactory.create(
            GsonBuilder().setLenient().create()
        ))
        .baseUrl("http://185.12.95.242/")
        .build()

data class AccessToken(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("token_type")
    val tokenType: String?,
    @SerializedName("expires_in")
    val expiresIn: Int?,
    @SerializedName("refresh_token")
    val refreshToken: String?,
    @SerializedName("scope")
    val scope: String?,
    @SerializedName("client_id")
    val clientID: String?,
    @SerializedName("client_secret")
    val clientSecret: String?
)
