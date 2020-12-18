package com.github.onotoliy.opposite.treasure.di.restful.resource

import com.github.onotoliy.opposite.data.page.Page
import retrofit2.Response

interface Resource<T> {
    fun getVersion(): String
    fun getAll(version: Long, offset: Int, numberOfRows: Int): Response<Page<T>>
    fun saveOrUpdate(dto: T): Response<T> {
        throw UnsupportedOperationException()
    }
}
