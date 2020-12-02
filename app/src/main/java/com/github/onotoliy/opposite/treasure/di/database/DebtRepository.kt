package com.github.onotoliy.opposite.treasure.di.database

import android.content.ContentValues
import android.database.Cursor
import com.github.onotoliy.opposite.data.Debt
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Event
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Meta
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.data.page.Paging
import com.github.onotoliy.opposite.treasure.utils.getBigDecimal
import com.github.onotoliy.opposite.treasure.utils.getString
import com.github.onotoliy.opposite.treasure.utils.getOption

class DebtRepository(database: SQLiteDatabase): AbstractRepository<Debt>(
    table = "treasure_debt",
    columns = listOf(
        "event_uuid",
        "event_total",
        "event_contribution",
        "event_name",
        "event_deadline",
        "event_creation_date",
        "event_author_uuid",
        "event_author_name",
        "deposit_user_uuid",
        "deposit_user_name",
        "deposit_deposit"
    ),
    database = database
) {

    override fun get(pk: String): Debt = throw UnsupportedOperationException()

    override fun merge(dto: Debt, local: Boolean) = throw UnsupportedOperationException()

    override fun update(dto: Debt, local: Boolean) = throw UnsupportedOperationException()

    override fun toDTO(cursor: Cursor): Debt = throw UnsupportedOperationException()

    fun deleteAll() {
        database.writableDatabase.delete("treasure_debt", "1 = 1", null)
    }

    override fun insert(dto: Debt, local: Boolean) = insert(
        ContentValues().apply {
            put("deposit_user_uuid", dto.deposit.uuid)
            put("deposit_user_name", dto.deposit.name)
            put("deposit_deposit", dto.deposit.deposit.toDouble())
            put("event_uuid", dto.event.uuid)
            put("event_total", dto.event.total.toDouble())
            put("event_contribution", dto.event.contribution.toDouble())
            put("event_name", dto.event.name)
            put("event_deadline", dto.event.deadline)
            put("event_creation_date", dto.event.creationDate)
            put("event_author_uuid", dto.event.author.uuid)
            put("event_author_name", dto.event.author.name)
        }
    )

    fun getDebtorAllOption(
        event: String,
        name: String?
    ): List<Option> {
        val whereClause = mutableListOf("event_uuid = ?")
        val whereArgs = mutableListOf(event)

        name.let {
            whereClause.add("name LIKE ?")
            whereArgs.add("%$it%")
        }

        return getAllOption(
            listOf("deposit_user_uuid", "deposit_user_name"),
            whereClause.joinToString(" AND "),
            whereArgs.toTypedArray()
        ) {
            getOption("deposit_user")
        }
    }

    fun getDebtorAll(
        event: String,
        offset: Int,
        limit: Int
    ): Page<Deposit> = getAll(
        offset = offset,
        limit = limit,
        whereClause = "event_uuid = ?",
        whereArgs = arrayOf(event),
        toDTO = {
            Deposit(
                person = it.getOption("deposit_user"),
                deposit = it.getBigDecimal("deposit_deposit")
            )
        }
    )

    fun getDebtAllOption(
        person: String,
        name: String?
    ): List<Option> {
        val whereClause = mutableListOf("event_uuid = ?")
        val whereArgs = mutableListOf(person)

        name.let {
            whereClause.add("name LIKE ?")
            whereArgs.add("%$it%")
        }

        return getAllOption(
            listOf("event_uuid", "event_name"),
            whereClause.joinToString(" AND "),
            whereArgs.toTypedArray()
        ) {
            getOption("event")
        }
    }

    fun getDebtAll(
        deposit: String,
        offset: Int,
        limit: Int
    ): Page<Event> = getAll(
        offset = offset,
        limit = limit,
        whereClause = "deposit_user_uuid = ?",
        whereArgs = arrayOf(deposit),
        toDTO = {
            Event(
                uuid = it.getString("event_uuid"),
                name = it.getString("event_name"),
                total = it.getBigDecimal("event_total"),
                contribution = it.getBigDecimal("event_contribution"),
                deadline = it.getString("event_deadline"),
                creationDate = it.getString("event_creation_date"),
                author = it.getOption("event_author")
            )
        }
    )

    private fun <T> getAll(
        offset: Int,
        limit: Int,
        whereClause: String = "1 = 1",
        whereArgs: Array<String> = arrayOf(),
        toDTO: (Cursor) -> T
    ): Page<T> = database
        .readableDatabase
        .rawQuery(
            "SELECT ${columns.joinToString()} FROM treasure_debt WHERE $whereClause LIMIT $offset, ${limit + offset + 1}",
            whereArgs
        )
        .run {
            val list = mutableListOf<T>()

            moveToFirst()

            while (moveToNext()) {
                list.add(toDTO(this))
            }

            Page(
                meta = Meta(total = count(), paging = Paging(offset + limit, limit)),
                context = list
            )
        }

    private fun count(
        whereClause: String = "1 = 1",
        whereArgs: Array<String> = arrayOf(),
    ) = database
        .readableDatabase
        .rawQuery("SELECT COUNT(*) AS total FROM treasure_debt WHERE $whereClause", whereArgs)
        .run {
            moveToFirst()

            getInt(getColumnIndex("total"))
        }
}
