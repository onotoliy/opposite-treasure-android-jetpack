package com.github.onotoliy.opposite.treasure.services

import com.github.onotoliy.opposite.treasure.resources.KeycloakResource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun token(username: String, password: String): String? = keycloak
    .auth(username = username, password = password).execute().body()?.access_token

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
