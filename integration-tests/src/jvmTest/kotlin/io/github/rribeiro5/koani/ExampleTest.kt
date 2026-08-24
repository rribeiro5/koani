package io.github.rribeiro5.koani

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertNotNull

class ExampleTest : BaseIntegrationTest() {

    @Test
    fun `smoke test - client can be instantiated`() {
        runBlocking {
            val client = createClient()
            
            // Demonstrating the rate-limited request pattern
            val result = performRequest {
                // In a real integration test, this would be: client.anime.getAnimeDetails(...)
                client
            }
            
            assertNotNull(result)
        }
    }
}
