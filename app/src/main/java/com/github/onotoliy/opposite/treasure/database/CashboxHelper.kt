package com.github.onotoliy.opposite.treasure.database

import android.content.ContentValues
import android.database.Cursor
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.data.page.Page

class CashboxHelper(helper: SQLiteHelper): AbstractHelper<Cashbox>(
    table = "treasure_cashbox",
    columns = listOf("deposit", "last_update_date"),
    helper = helper
) {
    fun get(): Cashbox = get(whereClause = "1 = 1", whereArgs = arrayOf())

    override fun get(pk: String): Cashbox = throw UnsupportedOperationException()

    override fun getAll(offset: Int, limit: Int): Page<Cashbox> = throw UnsupportedOperationException()

    override fun merge(dto: Cashbox) {
        if (exists()) {
            update(dto)
        } else {
            insert(dto)
        }
    }

    override fun insert(dto: Cashbox) = insert(ContentValues().apply {
        put("last_update_date", dto.lastUpdateDate)
        put("deposit", dto.deposit)
    })

    override fun update(dto: Cashbox) = update(ContentValues().apply {
        put("last_update_date", dto.lastUpdateDate)
        put("deposit", dto.deposit)
    })

    override fun toDTO(cursor: Cursor): Cashbox = cursor.run {
        Cashbox(
            lastUpdateDate = getString("last_update_date"),
            deposit = getBigDecimal("deposit")
        )
    }
}