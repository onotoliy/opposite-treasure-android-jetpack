package com.github.onotoliy.opposite.treasure.di.restful.retrofit

import com.github.onotoliy.opposite.treasure.utils.AccessToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface KeycloakRetrofit {

    @FormUrlEncoded
    @POST("/auth/realms/treasure/protocol/openid-connect/token")
    fun auth(
        @Field("client_id") clientId: String = "frontend",
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password"
    ): Call<AccessToken>

}
