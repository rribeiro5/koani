package io.github.rribeiro5.koani

import kotlin.test.Test
import kotlin.test.assertNotNull

class ExampleTest : BaseIntegrationTest() {

    @Test
    fun `smoke test - client can be instantiated`() {
        val client = createClient()
        assertNotNull(client)
    }
}
