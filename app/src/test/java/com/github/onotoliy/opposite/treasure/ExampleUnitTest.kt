package com.github.onotoliy.opposite.treasure

import androidx.compose.ui.text.AnnotatedString
import com.github.onotoliy.opposite.treasure.ui.components.MoneyVisualTransformation
import com.github.onotoliy.opposite.treasure.ui.components.TelephoneVisualTransformation
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_TelephoneVisualTransformation() {
        val transformation = TelephoneVisualTransformation()
        assertEquals("", transformation.filter(AnnotatedString("")).transformedText.text)
        assertEquals("(9", transformation.filter(AnnotatedString("9")).transformedText.text)
        assertEquals("(91", transformation.filter(AnnotatedString("91")).transformedText.text)
        assertEquals("(914) ", transformation.filter(AnnotatedString("914")).transformedText.text)
        assertEquals("(914) 6", transformation.filter(AnnotatedString("9146")).transformedText.text)
        assertEquals("(914) 63", transformation.filter(AnnotatedString("91463")).transformedText.text)
        assertEquals("(914) 638 ", transformation.filter(AnnotatedString("914638")).transformedText.text)
        assertEquals("(914) 638 1", transformation.filter(AnnotatedString("9146381")).transformedText.text)
        assertEquals("(914) 638 10 ", transformation.filter(AnnotatedString("91463810")).transformedText.text)
        assertEquals("(914) 638 10 7", transformation.filter(AnnotatedString("914638107")).transformedText.text)
        assertEquals("(914) 638 10 79", transformation.filter(AnnotatedString("9146381079")).transformedText.text)
    }

    @Test
    fun test_MoneyVisualTransformation() {
        val transformation = MoneyVisualTransformation()
        assertEquals("", transformation.filter(AnnotatedString("")).transformedText.text)
        assertEquals("1", transformation.filter(AnnotatedString("1")).transformedText.text)
        assertEquals("12", transformation.filter(AnnotatedString("12")).transformedText.text)
        assertEquals("123", transformation.filter(AnnotatedString("123")).transformedText.text)
        assertEquals("1 234", transformation.filter(AnnotatedString("1234")).transformedText.text)
        assertEquals("12 345", transformation.filter(AnnotatedString("12345")).transformedText.text)
        assertEquals("123 456", transformation.filter(AnnotatedString("123456")).transformedText.text)
        assertEquals("1 234 567", transformation.filter(AnnotatedString("1234567")).transformedText.text)
        assertEquals("12 345 678", transformation.filter(AnnotatedString("12345678")).transformedText.text)
        assertEquals("123 456 789", transformation.filter(AnnotatedString("123456789")).transformedText.text)

        assertEquals("123 456 789.000", transformation.filter(AnnotatedString("123456789.000")).transformedText.text)
    }
}