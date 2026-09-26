package io.github.rribeiro5.koani

import io.github.rribeiro5.koani.auth.MemoryTokenManager
import io.github.rribeiro5.koani.auth.TokenManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
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
        assertEquals(null, subject.clientSecret)
        assertEquals(null, subject.timeoutMillis)
        assertEquals(LogLevel.NONE, subject.logLevel)
        assertIs<MemoryTokenManager>(subject.tokenManager)
    }

    @Test
    fun `clientSecret should update builder property`() {
        val subject = createSubject()
        val clientSecret = "test-secret"

        subject.clientSecret(clientSecret)

        assertEquals(clientSecret, subject.clientSecret)
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
    fun `tokenManager should update builder property`() {
        val subject = createSubject()
        val customManager = object : TokenManager {
            override fun accessToken(): String? = null
            override fun refreshToken(): String? = null
            override fun storeTokens(accessToken: String, refreshToken: String?) {}
            override fun clearTokens() {}
        }

        subject.tokenManager(customManager)

        assertEquals(customManager, subject.tokenManager)
    }

    @Test
    fun `build should create KoaniClient`() {
        val subject = createSubject()

        val client = subject.build()

        assertNotNull(client)
    }

    @Test
    fun `build should throw exception when clientId is blank`() {
        val subject = createSubject(clientId = "")

        val exception = assertFailsWith<IllegalArgumentException> {
            subject.build()
        }

        assertEquals("Client ID cannot be empty", exception.message)
    }
}
