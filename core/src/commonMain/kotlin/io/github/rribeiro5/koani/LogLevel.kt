package io.github.rribeiro5.koani

/**
 * Represents the severity levels for logging.
 */
public enum class LogLevel {
    /**
     * Verbose logging for detailed information.
     */
    VERBOSE,
    /**
     * Debug logging for development-time troubleshooting.
     */
    DEBUG,
    /**
     * Information logging for general application flow.
     */
    INFO,
    /**
     * Warning logging for potential issues.
     */
    WARN,
    /**
     * Error logging for critical failures.
     */
    ERROR,
    /**
     * Assert logging for conditions that should never happen.
     */
    ASSERT,
    /**
     * No logging.
     */
    NONE
}
