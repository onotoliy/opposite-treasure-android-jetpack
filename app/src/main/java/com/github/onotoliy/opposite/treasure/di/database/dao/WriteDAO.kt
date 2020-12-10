package com.github.onotoliy.opposite.treasure.di.database.dao

interface WriteDAO<T> {
    fun replace(vo: T)
}
