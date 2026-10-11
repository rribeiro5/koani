package io.github.rribeiro5.koani

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KoaniClientKtTest {

    @Test
    fun `Builder DSL should build without configuration block`() {
        val client = KoaniClient(clientId = "123456789")

        assertNotNull(client)
    }

    @Test
    fun `Builder DSL should apply configuration block`() {
        var blockApplied = false
        val client = KoaniClient(clientId = "123456789") {
            blockApplied = true
            clientSecret("test-secret")
            timeoutMillis(5000L)
            logLevel(LogLevel.DEBUG)
        }

        assertNotNull(client)
        assertTrue(blockApplied)
    }
}
