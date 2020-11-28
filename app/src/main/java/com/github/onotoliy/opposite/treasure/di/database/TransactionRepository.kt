package com.github.onotoliy.opposite.treasure.di.database

import android.content.ContentValues
import android.database.Cursor
import com.github.onotoliy.opposite.data.Transaction
import com.github.onotoliy.opposite.data.TransactionType
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.utils.getString
import com.github.onotoliy.opposite.treasure.utils.getOption
import com.github.onotoliy.opposite.treasure.utils.getOptionOrNull

class TransactionRepository(database: SQLiteDatabase): AbstractRepository<Transaction>(
    table = "treasure_transaction",
    columns = listOf(
        "uuid",
        "name",
        "cash",
        "creation_date",
        "author_uuid",
        "author_name",
        "type",
        "event_uuid",
        "event_name",
        "person_uuid",
        "person_name"
    ),
    database = database
) {
    override fun get(pk: String): Transaction = get(
        whereClause = "uuid = ?",
        whereArgs = arrayOf(pk)
    )

    fun getAll(
        event: String?, person: String?, offset: Int, limit: Int
    ): Page<Transaction> {
        val whereClause = mutableListOf<String>()
        val whereArgs = mutableListOf<String>()

        whereClause.add("1 = 1")

        event?.let {
            whereClause.add("event_uuid = ?")
            whereArgs.add(it)
        }
        person?.let {
            whereClause.add("person_uuid = ?")
            whereArgs.add(it)
        }

        return getAll(
            whereClause = whereClause.joinToString(separator = " AND "),
            whereArgs = whereArgs.toTypedArray(),
            offset = offset,
            limit = limit
        )
    }

    override fun merge(dto: Transaction) {
        if (exists(whereClause = "uuid = ?", whereArgs = arrayOf(dto.uuid))) {
            update(dto)
        } else {
            insert(dto)
        }
    }

    override fun insert(dto: Transaction) = insert(ContentValues().apply {
        put("uuid", dto.uuid)
        put("name", dto.name)
        put("cash", dto.cash.toDouble())
        put("creation_date", dto.creationDate)
        put("author_uuid", dto.author.uuid)
        put("author_name", dto.author.name)
        put("type", dto.type.name)
        put("event_uuid", dto.event?.uuid)
        put("event_name", dto.event?.name)
        put("person_uuid", dto.person?.uuid)
        put("person_name", dto.person?.name)
    })

    override fun update(dto: Transaction) = update(ContentValues().apply {
        put("name", dto.name)
        put("cash", dto.cash.toDouble())
        put("creation_date", dto.creationDate)
        put("author_uuid", dto.author.uuid)
        put("author_name", dto.author.name)
        put("type", dto.type.name)
        put("event_uuid", dto.event?.uuid)
        put("event_name", dto.event?.name)
        put("person_uuid", dto.person?.uuid)
        put("person_name", dto.person?.name)
    })

    override fun toDTO(cursor: Cursor): Transaction = cursor.run {
        Transaction(
            uuid = getString("uuid"),
            name = getString("name"),
            cash = getString("cash"),
            creationDate = getString("creation_date"),
            type = TransactionType.valueOf(getString("type")),
            author = getOption("author"),
            event = getOptionOrNull("event"),
            person = getOptionOrNull("person")
        )
    }
}