package io.github.rribeiro5.koani

import kotlin.test.Test
import kotlin.test.assertNotNull

class ExampleTest : BaseIntegrationTest() {

    @Test
    fun `smoke test - client can be instantiated`() = runIntegrationTest { client ->
        // Demonstrating the rate-limited request pattern using the provided client
        val result = performRequest {
            // In a real integration test, this would be: client.anime.getAnimeDetails(...)
            client
        }
        
        assertNotNull(result)
    }
}
