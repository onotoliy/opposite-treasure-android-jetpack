package com.github.onotoliy.opposite.treasure.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMap
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class TelephoneVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val q1 = filter(text = text.text, startIndex = 0, endIndex = 3) {
            if (it.length == 3) {
                "($it)"
            } else {
                if (it.isEmpty()) { "" } else { "($it" }
            }
        }
        val q2 = filter(text = text.text, startIndex = 3, endIndex = 6)
        val q3 = filter(text = text.text, startIndex = 6, endIndex = 8)
        val q4 = filter(text = text.text, startIndex = 8, endIndex = 10)

        return TransformedText(
            AnnotatedString(q1 + q2 + q3 + q4),
            OffsetMap.identityOffsetMap
        )
    }

    private fun filter(
        text: String,
        startIndex: Int,
        endIndex: Int,
        convert: (String) -> String = { " $it" }
    ): String =
        if (text.length >= endIndex) {
            convert(text.substring(startIndex, endIndex))
        } else {
            if (text.length >= startIndex) {
                convert(text.substring(startIndex, text.length))
            } else {
                ""
            }
        }
}
