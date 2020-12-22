package com.github.onotoliy.opposite.treasure.di.database.repositories

import androidx.lifecycle.LiveData
import com.github.onotoliy.opposite.data.core.HasUUID
import com.github.onotoliy.opposite.treasure.di.database.dao.VersionDAO
import com.github.onotoliy.opposite.treasure.di.database.dao.WriteDAO
import com.github.onotoliy.opposite.treasure.di.database.data.VersionVO

open class AbstractRepository<T: HasUUID, DAO: WriteDAO<T>>(
    private val type: String,
    private val version: VersionDAO,
    protected val dao: DAO
) {
    fun getVersion(): String = version.get(type)?.version ?: "0"

    fun setVersion(v: String) = version.replace(VersionVO(type, v))

    fun clean() {
        setVersion("0")
        dao.clean()
    }

    fun replace(vo: T) = dao.replace(vo)

    fun getAllLocal(): List<T> = dao.getAllLocal()

    fun get(pk: String): LiveData<T> = dao.get(pk)

    fun getAll(offset: Int, numberOfRows: Int): LiveData<List<T>> =
        dao.getAll(offset, numberOfRows)

    fun count(): LiveData<Long> = dao.count()
}
