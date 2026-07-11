package io.github.rribeiro5.koani.util

private const val SANITIZATION_THRESHOLD = 4

/**
 * Sanitizes a sensitive string by masking most of its characters.
 */
internal fun String.sanitize(): String {
    return if (length > SANITIZATION_THRESHOLD) {
        take(SANITIZATION_THRESHOLD) + "*".repeat(length - SANITIZATION_THRESHOLD)
    } else {
        "*".repeat(length)
    }
}
