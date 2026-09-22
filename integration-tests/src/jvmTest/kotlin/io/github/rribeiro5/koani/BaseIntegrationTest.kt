package io.github.rribeiro5.koani

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import io.github.rribeiro5.koani.auth.MemoryTokenManager
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
    protected fun runIntegrationTest(
        authenticated: Boolean = false,
        block: suspend (KoaniClient) -> Unit,
    ) {
        runBlocking {
            val client = createClient(authenticated)
            block(client)
        }
    }

    /**
     * Executes a block (API request) while holding a global lock to respect rate limits.
     * The lock is held during the request and for a mandatory 1-second cooldown period.
     */
    protected suspend fun <T> performRequest(block: suspend () -> T): T {
        return apiMutex.withLock {
            val result = block()
            delay(1.seconds)
            result
        }
    }

    protected fun createClient(authenticated: Boolean = false): KoaniClient {
        val builder = KoaniClient.Builder(malClientId)
            .timeoutMillis(30_000) // Increase timeout for integration tests
        if (authenticated) {
            val accessToken = System.getProperty("TEST_MAL_ACCESS_TOKEN")
                ?: throw IllegalStateException(
                    "TEST_MAL_ACCESS_TOKEN system property is not set. " +
                        "Provide it via -PTEST_MAL_ACCESS_TOKEN, environment variable, or local.properties."
                )
            val refreshToken = System.getProperty("TEST_MAL_REFRESH_TOKEN")
                ?: throw IllegalStateException(
                    "TEST_MAL_REFRESH_TOKEN system property is not set. " +
                        "Provide it via -PTEST_MAL_REFRESH_TOKEN, environment variable, or local.properties."
                )
            val tokenManager = MemoryTokenManager().apply {
                storeTokens(accessToken, refreshToken)
            }
            builder.tokenManager(tokenManager)
        }
        return builder.build()
    }
}
