package com.github.onotoliy.opposite.treasure.di.database.dao

import androidx.lifecycle.LiveData

interface WriteDAO<T> {
    fun clean()
    fun replace(vo: T)
    fun getAllLocal(): List<T>
    fun get(pk: String): LiveData<T>
    fun getAll(offset: Int, numberOfRows: Int): LiveData<List<T>>
    fun count(): LiveData<Long>
}
