package io.github.rribeiro5.koani

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.time.Duration.Companion.seconds

abstract class BaseIntegrationTest {

    protected val malClientId: String
        get() = System.getProperty("TEST_MAL_CLIENT_ID")
            ?: throw IllegalStateException("TEST_MAL_CLIENT_ID system property is not set. Please provide it via -PTEST_MAL_CLIENT_ID, environment variable, or local.properties.")

    @BeforeTest
    fun setup() = runBlocking {
        // Mandatory delay to respect MAL API rate limits (approx 1 request per second safe margin)
        delay(2.seconds)
    }

    protected fun createClient(): KoaniClient {
        return KoaniClient.Builder(malClientId)
            .build()
    }
}
