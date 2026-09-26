package io.github.rribeiro5.koani.core.mapper

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DateTimeMapperTest {

    @Test
    fun `toDateTime should parse ISO 8601 format`() {
        val dateTimeStr = "2016-12-07T12:34:56+09:00"
        val result = dateTimeStr.toDateTime()
        assertNotNull(result)
        // Instant.parse handles the offset and stores as UTC
    }

    @Test
    fun `toDateTime should return null for invalid format`() {
        assertNull("invalid".toDateTime())
    }

    @Test
    fun `toDate should parse YYYY-MM-DD`() {
        val dateStr = "2023-10-27"
        val result = dateStr.toDate()
        assertEquals(LocalDate(2023, 10, 27), result)
    }

    @Test
    fun `toDate should parse YYYY-MM`() {
        val dateStr = "2023-10"
        val result = dateStr.toDate()
        assertEquals(LocalDate(2023, 10, 1), result)
    }

    @Test
    fun `toDate should parse YYYY`() {
        val dateStr = "2023"
        val result = dateStr.toDate()
        assertEquals(LocalDate(2023, 1, 1), result)
    }

    @Test
    fun `toDate should return null for invalid format`() {
        assertNull("2023-10-27-01".toDate())
        assertNull("abc".toDate())
    }

    @Test
    fun `toTime should parse HH-MM-SS`() {
        val timeStr = "12:34:56"
        val result = timeStr.toTime()
        assertEquals(LocalTime(12, 34, 56), result)
    }

    @Test
    fun `toTime should parse HH-MM`() {
        val timeStr = "12:34"
        val result = timeStr.toTime()
        assertEquals(LocalTime(12, 34, 0), result)
    }

    @Test
    fun `toTime should return null for invalid format`() {
        assertNull("invalid".toTime())
    }
}
