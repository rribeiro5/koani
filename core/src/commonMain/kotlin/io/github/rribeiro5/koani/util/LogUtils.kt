package io.github.rribeiro5.koani.util

/**
 * Sanitizes a sensitive string by masking most of its characters.
 */
internal fun String.sanitize(): String {
    return if (length > 4) {
        take(4) + "*".repeat(length - 4)
    } else {
        "*".repeat(length)
    }
}
