package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMap
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MoneyVisualTransformation: VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val indexOf = text.text.indexOf(".")

        val money = if (indexOf == -1) {
            text.text
        } else {
            text.text.substring(0, indexOf)
        }

        val cent = if (indexOf == -1) {
            ""
        } else {
            text.text.substring(indexOf)
        }

        return TransformedText(
            AnnotatedString(filter(money) + cent),
            OffsetMap.identityOffsetMap
        )
    }

    private fun filter(text: String): String =
        if (text.length >= 3) {
            val index = text.length - 3
            val thousand = filter(text.substring(0, index))
            val hundred = text.substring(index)

            if (thousand.isEmpty()) {
                hundred
            } else {
                "$thousand $hundred"
            }
        } else {
            text.substring(0, text.length)
        }

}
