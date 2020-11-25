package com.github.onotoliy.opposite.treasure.di.database

import android.content.ContentValues
import android.database.Cursor
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Page

class DepositRepository(database: SQLiteDatabase): AbstractRepository<Deposit>(
    table = "treasure_deposit",
    columns = listOf("user_uuid", "user_name", "deposit"),
    database = database
) {
    override fun get(pk: String): Deposit = get(
        whereClause = "user_uuid = ?",
        whereArgs = arrayOf(pk)
    )

    fun getAll(offset: Int, limit: Int): Page<Deposit> = getAll(offset, limit)

    override fun merge(dto: Deposit) {
        if (exists(whereClause = "user_uuid = ?", whereArgs = arrayOf(dto.uuid))) {
            update(dto)
        } else {
            insert(dto)
        }
    }

    override fun insert(dto: Deposit) = insert(ContentValues().apply {
        put("user_uuid", dto.person.uuid)
        put("user_name", dto.person.name)
        put("deposit", dto.deposit.toDouble())
    })

    override fun update(dto: Deposit) = update(ContentValues().apply {
        put("user_name", dto.person.name)
        put("deposit", dto.deposit.toDouble())
    })

    override fun toDTO(cursor: Cursor): Deposit = cursor.run {
        Deposit(
            person = getOption("user"),
            deposit = getBigDecimal("deposit")
        )
    }
}
