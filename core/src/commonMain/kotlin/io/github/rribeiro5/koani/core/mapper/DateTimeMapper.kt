package io.github.rribeiro5.koani.core.mapper

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Instant

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
            3 -> LocalDate.parse(this)
            2 -> LocalDate(parts[0].toInt(), parts[1].toInt(), 1)
            1 -> if (this.length == 4) LocalDate(this.toInt(), 1, 1) else null
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
