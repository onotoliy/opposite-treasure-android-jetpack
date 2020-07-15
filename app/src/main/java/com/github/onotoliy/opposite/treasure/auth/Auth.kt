package com.github.onotoliy.opposite.treasure.auth

import android.os.AsyncTask
import com.fasterxml.jackson.databind.ObjectMapper
import java.net.HttpURLConnection
import java.net.URL

const val ACCOUNT_TYPE = "com.github.onotoliy.opposite.treasure"

fun asyncAuthToken(username: String, password: String): String? =
    try {
        Auth().execute(username, password).get()
    } catch (e : Exception) {
        null
    }

class Auth : AsyncTask<String, String, String>() {
    override fun doInBackground(vararg params: String): String {
        return authToken(params[0], params[1])
    }
}

fun authToken(username: String, password: String): String {
    val url = URL("http://185.12.95.242/auth/realms/treasure/protocol/openid-connect/token")
    val parameters =
        "client_id=frontend&username=$username&password=$password&grant_type=password".toByteArray()

    val connection = url.openConnection() as HttpURLConnection
    connection.doOutput = true
    connection.instanceFollowRedirects = false
    connection.requestMethod = "POST"
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
    connection.setRequestProperty("charset", "utf-8")
    connection.setRequestProperty("Content-Length", parameters.size.toString())
    connection.useCaches = false;
    connection.outputStream.use {
        it.write(parameters)
    }

    val content = String(connection.inputStream.readBytes())

    return ObjectMapper().readTree(content)["access_token"].asText()
}