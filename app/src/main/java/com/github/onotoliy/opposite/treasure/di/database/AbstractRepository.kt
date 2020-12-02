package com.github.onotoliy.opposite.treasure.di.database

import android.content.ContentValues
import android.database.Cursor
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import com.github.onotoliy.opposite.treasure.utils.getString

abstract class AbstractRepository<T>(
    protected val table: String,
    protected val columns: List<String>,
    protected val database: SQLiteDatabase
) {
    abstract fun get(pk: String): T
    abstract fun merge(dto: T, local: Boolean = false)
    abstract fun insert(dto: T, local: Boolean = false)
    abstract fun update(dto: T, local: Boolean = false)

    protected abstract fun toDTO(cursor: Cursor): T

    fun beginTransaction() = database.writableDatabase.beginTransaction()
    fun endTransaction() = database.writableDatabase.endTransaction()
    fun setTransactionSuccessful() = database.writableDatabase.setTransactionSuccessful()
    fun version(): Int = 0

    protected fun merge(
        values: ContentValues,
        whereClause: String = "uuid = ?",
        whereArgs: Array<String> = arrayOf()
    ) {
        if (exists(whereClause, whereArgs)) {
            update(values)
        } else {
            insert(values)
        }
    }

    protected fun insert(values: ContentValues) {
        database.writableDatabase.insert(table, null, values)
    }

    protected fun update(
        values: ContentValues,
        whereClause: String = "uuid = ?",
        whereArgs: Array<String> = arrayOf()
    ) {
        database.writableDatabase.update(table, values, whereClause, whereArgs)
    }

    protected fun get(
        whereClause: String = "uuid = ?",
        whereArgs: Array<String> = arrayOf()
    ): T = database
        .readableDatabase
        .rawQuery("SELECT ${columns.joinToString()} FROM $table WHERE $whereClause", whereArgs)
        .run {
            moveToFirst()

            toDTO(this)
        }

    protected fun getAll(
        offset: Int,
        limit: Int,
        whereClause: String = "1 = 1",
        whereArgs: Array<String> = arrayOf()
    ): Page<T> = database
        .readableDatabase
        .rawQuery("SELECT ${columns.joinToString()} FROM $table WHERE $whereClause LIMIT $offset, ${limit + offset + 1}", whereArgs)
        .run {
            val list = mutableListOf<T>()

            if (count > 0) {
                moveToFirst()

                do {
                    list.add(toDTO(this))
                } while (moveToNext())
            }

            Page(meta = Meta(total = count(), paging = Paging(offset + limit, limit)), context = list)
        }

    protected fun getAllOption(
        columns: List<String> = listOf("uuid", "name"),
        whereClause: String = "1 = 1",
        whereArgs: Array<String> = arrayOf(),
        toDTO: Cursor.() -> Option
    ): List<Option> = database
        .readableDatabase
        .rawQuery("SELECT ${columns.joinToString(", ")} FROM $table WHERE $whereClause", whereArgs)
        .run {
            val list = mutableListOf<Option>()

            if (count > 0) {
                moveToFirst()

                do {
                    list.add(toDTO())
                } while (moveToNext())
            }

            list
        }

    private fun count() = database
        .readableDatabase
        .rawQuery("SELECT COUNT(*) AS total FROM $table", null)
        .run {
            moveToFirst()

            getInt(getColumnIndex("total"))
        }

    protected fun exists(
        whereClause: String = "uuid = ?",
        whereArgs: Array<String> = arrayOf()
    ): Boolean = database
        .readableDatabase
        .rawQuery("SELECT COUNT(*) AS total FROM $table WHERE $whereClause", whereArgs)
        .run {
            moveToFirst()

            getInt(getColumnIndex("total")) == 1
        }
}
