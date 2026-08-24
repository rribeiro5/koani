package io.github.rribeiro5.koani

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration.Companion.seconds

abstract class BaseIntegrationTest {

    companion object {
        private val apiMutex = Mutex()
    }

    protected val malClientId: String
        get() = System.getProperty("TEST_MAL_CLIENT_ID")
            ?: throw IllegalStateException("TEST_MAL_CLIENT_ID system property is not set. Please provide it via -PTEST_MAL_CLIENT_ID, environment variable, or local.properties.")

    /**
     * Helper to run an integration test block in a blocking coroutine scope.
     * Provides a fresh KoaniClient instance to the test block.
     */
    protected fun runIntegrationTest(block: suspend (KoaniClient) -> Unit) {
        runBlocking {
            val client = createClient()
            block(client)
        }
    }

    /**
     * Executes a block (API request) while holding a global lock to respect rate limits.
     * The lock is held during the request and for a mandatory 2-second cooldown period.
     */
    protected suspend fun <T> performRequest(block: suspend () -> T): T {
        return apiMutex.withLock {
            val result = block()
            delay(2.seconds)
            result
        }
    }

    protected fun createClient(): KoaniClient {
        return KoaniClient.Builder(malClientId)
            .build()
    }
}
