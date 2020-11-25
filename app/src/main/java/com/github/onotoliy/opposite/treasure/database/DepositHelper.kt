package com.github.onotoliy.opposite.treasure.database

import android.content.ContentValues
import android.database.Cursor
import com.github.onotoliy.opposite.data.Deposit
import com.github.onotoliy.opposite.data.page.Page

class DepositHelper(helper: SQLiteHelper): AbstractHelper<Deposit>(
    table = "treasure_deposit",
    columns = listOf("user_uuid", "user_name", "deposit"),
    helper = helper
) {
    override fun get(pk: String): Deposit = get(
        whereClause = "user_uuid = ?",
        whereArgs = arrayOf(pk)
    )

    override fun getAll(offset: Int, limit: Int): Page<Deposit> = getAll(offset, limit)

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
