package io.github.rribeiro5.koani

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class KoaniClientBuilderTest {

    private fun createSubject(clientId: String = "123456789"): KoaniClient.Builder {
        return KoaniClient.Builder(clientId)
    }

    @Test
    fun `builder should have correct default values`() {
        val clientId = "test-client-id"
        val subject = createSubject(clientId)

        assertEquals(clientId, subject.clientId)
        assertEquals(null, subject.timeoutMillis)
        assertEquals(LogLevel.NONE, subject.logLevel)
    }

    @Test
    fun `timeoutMillis should update builder property`() {
        val subject = createSubject()
        val timeout = 5000L

        subject.timeoutMillis(timeout)

        assertEquals(timeout, subject.timeoutMillis)
    }

    @Test
    fun `logLevel should update builder property`() {
        val subject = createSubject()
        val logLevel = LogLevel.DEBUG

        subject.logLevel(logLevel)

        assertEquals(logLevel, subject.logLevel)
    }

    @Test
    fun `build should create KoaniClient`() {
        val subject = createSubject()
        
        val client = subject.build()

        assertNotNull(client)
    }
}
