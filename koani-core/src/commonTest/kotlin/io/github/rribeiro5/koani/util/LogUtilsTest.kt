package io.github.rribeiro5.koani.util

import kotlin.test.Test
import kotlin.test.assertEquals

class LogUtilsTest {

    @Test
    fun `sanitize should keep first 4 characters and mask the rest when length is greater than 4`() {
        val input = "12345678"
        val expected = "1234****"
        assertEquals(expected, input.sanitize())
    }

    @Test
    fun `sanitize should mask all characters when length is 4`() {
        val input = "1234"
        val expected = "****"
        assertEquals(expected, input.sanitize())
    }

    @Test
    fun `sanitize should mask all characters when length is less than 4`() {
        val input = "123"
        val expected = "***"
        assertEquals(expected, input.sanitize())
    }

    @Test
    fun `sanitize should return empty string when input is empty`() {
        val input = ""
        val expected = ""
        assertEquals(expected, input.sanitize())
    }
}
