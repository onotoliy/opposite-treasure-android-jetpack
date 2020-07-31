package com.github.onotoliy.opposite.treasure.resources

import com.github.onotoliy.opposite.data.Option
import retrofit2.Call
import retrofit2.http.GET

interface UserResource {
    @GET("/api/treasure/v1/user/current")
    fun getCurrentUser(): Call<Option>

    @GET("/api/treasure/v1/user/current/roles")
    fun getCurrentUserRoles(): Call<Set<String>>

    @GET("/api/treasure/v1/user/list")
    fun getAll(): Call<List<Option>>
}
