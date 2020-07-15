package com.github.onotoliy.opposite.treasure.tasks

import android.accounts.AccountManager
import android.content.Context
import android.os.AsyncTask
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.onotoliy.opposite.treasure.auth.authToken
import com.github.onotoliy.opposite.treasure.auth.getAuthToken
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

open class GetRequest<T>(
    private val link: String,
    private val manager: AccountManager,
    private val valueTypeRef: TypeReference<T>
): AsyncTask<Void, Void, T>() {


    override fun doInBackground(vararg params: Void?): T {
        val connection = URL(link).openConnection() as HttpURLConnection
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Authorization", "Bearer ${manager.getAuthToken()}")

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            return ObjectMapper().readValue(connection.inputStream, valueTypeRef)
        }

        throw IllegalArgumentException(
            "URL: $link. Status code: ${connection.responseCode}. Message: ${String(connection.errorStream.readBytes())}"
        )
    }
}