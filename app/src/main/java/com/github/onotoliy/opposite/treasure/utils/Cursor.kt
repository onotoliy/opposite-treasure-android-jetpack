package com.github.onotoliy.opposite.treasure.utils

import android.database.Cursor
import com.github.onotoliy.opposite.data.Option
import java.math.BigDecimal

fun Cursor.getOption(columnName: String): Option = Option(
    uuid = getString("${columnName}_uuid"),
    name = getString("${columnName}_name")
)
fun Cursor.getString(columnName: String): String =
    getStringOrNull(columnName) ?: ""
fun Cursor.getBigDecimal(columnName: String): String =
    getBigDecimalOrNull(columnName) ?: "0.0"

fun Cursor.getOptionOrNull(columnName: String): Option? {
    val uuid = getStringOrNull("${columnName}_uuid")
    val name = getStringOrNull("${columnName}_name")

    return if (uuid.isNullOrEmpty() || name.isNullOrEmpty()) {
        null
    } else {
        Option(uuid, name)
    }
}
fun Cursor.getStringOrNull(columnName: String): String? =
    getString(getColumnIndex(columnName))
fun Cursor.getBigDecimalOrNull(columnName: String): String? = BigDecimal
    .valueOf(getDouble(getColumnIndex(columnName)))
    .toPlainString()
