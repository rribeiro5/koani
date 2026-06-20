package io.github.rribeiro5.koani

import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Severity
import io.github.rribeiro5.koani.di.KoaniContainer
import io.github.rribeiro5.koani.di.assertContains
import io.github.rribeiro5.koani.di.fakeContainer
import io.github.rribeiro5.koani.di.fakeLogWriter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalKermitApi::class)
class KoaniClientTest {

    private fun createSubject(container: KoaniContainer): KoaniClient {
        return KoaniClient(container)
    }

    @Test
    fun `init should log successfully initialized`() {
        val logWriter = fakeLogWriter()
        val container = fakeContainer(
            logLevel = LogLevel.DEBUG,
            logWriter = logWriter,
        )

        createSubject(container)

        logWriter.assertContains {
            message == "KoaniClient successfully initialized"
        }
    }

    @Test
    fun `init should throw exception and log when clientId is blank`() {
        val logWriter = fakeLogWriter()
        val container = fakeContainer(
            clientId = " ",
            logWriter = logWriter,
            logLevel = LogLevel.ERROR
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            createSubject(container)
        }

        assertEquals("Client ID cannot be empty", exception.message)
        logWriter.assertContains {
            message == "Client ID cannot be empty" && severity == Severity.Error
        }
    }
}
