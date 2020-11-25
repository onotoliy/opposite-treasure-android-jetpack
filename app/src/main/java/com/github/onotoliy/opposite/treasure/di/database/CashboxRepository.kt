package com.github.onotoliy.opposite.treasure.di.database

import android.content.ContentValues
import android.database.Cursor
import com.github.onotoliy.opposite.data.Cashbox

class CashboxRepository(database: SQLiteDatabase): AbstractRepository<Cashbox>(
    table = "treasure_cashbox",
    columns = listOf("deposit", "last_update_date"),
    database = database
) {
    fun get(): Cashbox = get(whereClause = "1 = 1", whereArgs = arrayOf())

    override fun get(pk: String): Cashbox = throw UnsupportedOperationException()

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