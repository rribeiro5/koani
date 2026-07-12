package io.github.rribeiro5.koani.core.mapper

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Instant

private const val FULL_DATE_PARTS = 3
private const val YEAR_MONTH_PARTS = 2
private const val YEAR_ONLY_PARTS = 1
private const val YEAR_INDEX = 0
private const val MONTH_INDEX = 1
private const val YEAR_ONLY_LENGTH = 4

/**
 * Converts a string to [Instant]? according to ISO 8601 combined date and time format.
 * Example: 2016-12-07T12:34:56+09:00
 */
internal fun String.toDateTime(): Instant? {
    return try {
        Instant.parse(this)
    } catch (_: Exception) {
        null
    }
}

/**
 * Converts a string to [LocalDate]? according to ISO 8601 date format.
 * Supports YYYY-MM-DD, YYYY-MM, or YYYY.
 */
internal fun String.toDate(): LocalDate? {
    return try {
        val parts = this.split("-")
        when (parts.size) {
            FULL_DATE_PARTS -> LocalDate.parse(this)
            YEAR_MONTH_PARTS -> LocalDate(parts[YEAR_INDEX].toInt(), parts[MONTH_INDEX].toInt(), 1)
            YEAR_ONLY_PARTS -> if (this.length == YEAR_ONLY_LENGTH) LocalDate(this.toInt(), 1, 1) else null
            else -> null
        }
    } catch (_: Exception) {
        null
    }
}

/**
 * Converts a string to [LocalTime]? according to ISO 8601 time format.
 * Example: 12:34:56
 */
internal fun String.toTime(): LocalTime? {
    return try {
        LocalTime.parse(this)
    } catch (_: Exception) {
        null
    }
}
