package com.github.onotoliy.opposite.treasure.di.database.dao

interface WriteDAO<T> {
    fun clean()
    fun replace(vo: T)
    fun getAllLocal(): List<T>
}
