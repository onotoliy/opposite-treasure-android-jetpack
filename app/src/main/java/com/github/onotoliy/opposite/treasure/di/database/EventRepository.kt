package com.github.onotoliy.opposite.treasure.di.database

import android.content.ContentValues
import android.database.Cursor
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.utils.getBigDecimal
import com.github.onotoliy.opposite.treasure.utils.getString
import com.github.onotoliy.opposite.treasure.utils.getOption

class EventRepository(database: SQLiteDatabase) : AbstractRepository<Event>(
    table = "treasure_event",
    columns = listOf(
        "uuid",
        "total",
        "contribution",
        "name",
        "deadline",
        "creation_date",
        "author_uuid",
        "author_name"
    ),
    database = database
) {
    override fun get(pk: String): Event = get(
        whereClause = "uuid = ?",
        whereArgs = arrayOf(pk)
    )

    fun getAll(offset: Int, limit: Int): Page<Event> = getAll(offset, limit, "1 = 1", arrayOf())

    fun getAllOption(name: String?): List<Option> {
        val whereClause = mutableListOf("1 = 1")
        val whereArgs = mutableListOf<String>()

        name.let {
            whereClause.add("name LIKE ?")
            whereArgs.add("%$it%")
        }

        return getAllOption(
            listOf("uuid", "name"), whereClause.joinToString(" AND "), whereArgs.toTypedArray()
        ) {
            Option(getString("uuid"), getString("name"))
        }
    }

    override fun merge(dto: Event, local: Boolean) {
        if (exists(whereClause = "uuid = ?", whereArgs = arrayOf(dto.uuid))) {
            update(dto, local)
        } else {
            insert(dto, local)
        }
    }

    override fun insert(dto: Event, local: Boolean) = insert(ContentValues().apply {
        put("uuid", dto.uuid)
        put("total", dto.total.toDouble())
        put("contribution", dto.contribution.toDouble())
        put("name", dto.name)
        put("deadline", dto.deadline)
        put("creation_date", dto.creationDate)
        put("author_uuid", dto.author.uuid)
        put("author_name", dto.author.name)
    })

    override fun update(dto: Event, local: Boolean) = update(ContentValues().apply {
        put("total", dto.total.toDouble())
        put("contribution", dto.contribution.toDouble())
        put("name", dto.name)
        put("deadline", dto.deadline)
        put("creation_date", dto.creationDate)
        put("author_uuid", dto.author.uuid)
        put("author_name", dto.author.name)
    })

    override fun toDTO(cursor: Cursor): Event = cursor.run {
        Event(
            uuid = getString("uuid"),
            name = getString("name"),
            total = getBigDecimal("total"),
            contribution = getBigDecimal("contribution"),
            deadline = getString("deadline"),
            creationDate = getString("creation_date"),
            author = getOption("author")
        )
    }
}