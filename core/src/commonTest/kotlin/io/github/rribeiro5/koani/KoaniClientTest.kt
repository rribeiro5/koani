package io.github.rribeiro5.koani

import co.touchlab.kermit.ExperimentalKermitApi
import io.github.rribeiro5.koani.di.KoaniContainer
import io.github.rribeiro5.koani.di.assertContains
import io.github.rribeiro5.koani.di.fakeContainer
import io.github.rribeiro5.koani.di.fakeLogWriter
import kotlin.test.Test

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
}
