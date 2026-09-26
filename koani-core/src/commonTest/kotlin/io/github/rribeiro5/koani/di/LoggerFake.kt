package io.github.rribeiro5.koani.di

import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.TestConfig
import co.touchlab.kermit.TestLogWriter
import kotlin.test.assertTrue

@OptIn(ExperimentalKermitApi::class)
internal fun fakeLogWriter(): TestLogWriter = TestLogWriter(loggable = Severity.Verbose)

@OptIn(ExperimentalKermitApi::class)
internal fun fakeLogger(
    logWriter: LogWriter = fakeLogWriter()
): Logger = Logger(
    TestConfig(
        minSeverity = Severity.Verbose,
        logWriterList = listOf(logWriter)
    )
)

@OptIn(ExperimentalKermitApi::class)
internal fun TestLogWriter.assertContains(check: TestLogWriter.LogEntry.() -> Boolean) {
    assertTrue(logs.any { it.check() })
}
