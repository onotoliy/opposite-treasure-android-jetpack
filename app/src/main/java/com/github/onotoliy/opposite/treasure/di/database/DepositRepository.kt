package com.github.onotoliy.opposite.treasure.di.database

import android.content.ContentValues
import android.database.Cursor
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.Option
import com.github.onotoliy.opposite.data.page.Page
import com.github.onotoliy.opposite.treasure.utils.getBigDecimal
import com.github.onotoliy.opposite.treasure.utils.getOption

class DepositRepository(database: SQLiteDatabase) : AbstractRepository<Deposit>(
    table = "treasure_deposit",
    columns = listOf("user_uuid", "user_name", "deposit"),
    database = database
) {
    override fun get(pk: String): Deposit = get(
        whereClause = "user_uuid = ?",
        whereArgs = arrayOf(pk)
    )

    fun getAll(offset: Int, limit: Int): Page<Deposit> = getAll(
        offset = offset,
        limit = limit,
        whereClause = "1 = 1",
        whereArgs = arrayOf()
    )

    fun getAllOption(name: String?): List<Option> {
        val whereClause = mutableListOf("1 = 1")
        val whereArgs = mutableListOf<String>()

        name.let {
            whereClause.add("name LIKE ?")
            whereArgs.add("%$it%")
        }

        return getAllOption(
            listOf("user_uuid", "user_name"),
            whereClause.joinToString(" AND "),
            whereArgs.toTypedArray()
        ) {
            getOption("user")
        }
    }

    override fun merge(dto: Deposit, local: Boolean) {
        if (exists(whereClause = "user_uuid = ?", whereArgs = arrayOf(dto.uuid))) {
            update(dto, local)
        } else {
            insert(dto, local)
        }
    }

    override fun insert(dto: Deposit, local: Boolean) = insert(ContentValues().apply {
        put("user_uuid", dto.uuid)
        put("user_name", dto.name)
        put("deposit", dto.deposit.toDouble())
    })

    override fun update(dto: Deposit, local: Boolean) = update(
        whereClause = "user_uuid = ?",
        whereArgs = arrayOf(dto.uuid),
        values = ContentValues().apply {
            put("user_name", dto.name)
            put("deposit", dto.deposit.toDouble())
        }
    )

    override fun toDTO(cursor: Cursor): Deposit = cursor.run {
        if (cursor.count == 0) {
            Deposit()
        } else {
            Deposit(
                person = getOption("user"),
                deposit = getBigDecimal("deposit")
            )
        }
    }
}
