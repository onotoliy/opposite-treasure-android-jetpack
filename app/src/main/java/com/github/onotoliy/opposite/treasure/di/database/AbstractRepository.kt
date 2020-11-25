package com.github.onotoliy.opposite.treasure.di.database

import android.content.ContentValues
import android.database.Cursor
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import java.math.BigDecimal

abstract class AbstractRepository<T>(
    private val table: String,
    private val columns: List<String>,
    private val database: SQLiteDatabase
) {
    abstract fun get(pk: String): T
    abstract fun merge(dto: T)
    abstract fun insert(dto: T)
    abstract fun update(dto: T)

    protected abstract fun toDTO(cursor: Cursor): T

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

            moveToFirst()

            while (moveToNext()) {
                list.add(toDTO(this))
            }

            Page(meta = Meta(total = count(), paging = Paging(offset + limit, limit)), context = list)
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

    protected fun Cursor.getOption(columnName: String): Option = Option(
        uuid = getString("${columnName}_uuid"),
        name = getString("${columnName}_name")
    )
    protected fun Cursor.getString(columnName: String): String =
        getStringOrNull(columnName) ?: ""
    protected fun Cursor.getBigDecimal(columnName: String): String =
        getBigDecimalOrNull(columnName) ?: "0.0"

    protected fun Cursor.getOptionOrNull(columnName: String): Option? {
        val uuid = getStringOrNull("${columnName}_uuid")
        val name = getStringOrNull("${columnName}_name")

        return if (uuid.isNullOrEmpty() || name.isNullOrEmpty()) {
            null
        } else {
            Option(uuid, name)
        }
    }
    private fun Cursor.getStringOrNull(columnName: String): String? =
        getString(getColumnIndex(columnName))
    private fun Cursor.getBigDecimalOrNull(columnName: String): String? = BigDecimal
        .valueOf(getDouble(getColumnIndex(columnName)))
        .toPlainString()
}