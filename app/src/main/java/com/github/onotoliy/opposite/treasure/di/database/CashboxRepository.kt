package com.github.onotoliy.opposite.treasure.di.database

import android.content.ContentValues
import android.database.Cursor
import com.github.onotoliy.opposite.data.Cashbox
import com.github.onotoliy.opposite.treasure.utils.getString
import com.github.onotoliy.opposite.treasure.utils.getBigDecimal

class CashboxRepository(database: SQLiteDatabase): AbstractRepository<Cashbox>(
    table = "treasure_cashbox",
    columns = listOf("deposit", "last_update_date"),
    database = database
) {
    fun get(): Cashbox = get(whereClause = "1 = 1", whereArgs = arrayOf())

    override fun get(pk: String): Cashbox = throw UnsupportedOperationException()

    override fun merge(dto: Cashbox, local: Boolean) {
        if (exists(whereClause = "1 = 1", whereArgs = arrayOf())) {
            update(dto, local)
        } else {
            insert(dto, local)
        }
    }

    override fun insert(dto: Cashbox, local: Boolean) = insert(ContentValues().apply {
        put("last_update_date", dto.lastUpdateDate)
        put("deposit", dto.deposit)
    })

    override fun update(dto: Cashbox, local: Boolean) = update(
        whereClause = "1 = 1",
        whereArgs = arrayOf(),
        values = ContentValues().apply {
            put("last_update_date", dto.lastUpdateDate)
            put("deposit", dto.deposit)
        }
    )

    override fun toDTO(cursor: Cursor): Cashbox = cursor.run {
        if (cursor.count == 0) {
            Cashbox()
        } else {
            Cashbox(
                lastUpdateDate = getString("last_update_date"),
                deposit = getBigDecimal("deposit")
            )
        }
    }
}