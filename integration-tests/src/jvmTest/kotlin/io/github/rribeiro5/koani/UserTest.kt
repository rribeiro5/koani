package io.github.rribeiro5.koani

import io.github.rribeiro5.koani.user.UserField
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserTest : BaseIntegrationTest() {

    @Test
    fun `get authenticated user details`() = runIntegrationTest(authenticated = true) { client ->
        val user = performRequest {
            client.user.getUserDetails()
        }

        assertNotNull(user)
        assertTrue(user.id > 0)
        assertTrue(user.name.isNotBlank())
    }

    @Test
    fun `get authenticated user details with all fields`() =
        runIntegrationTest(authenticated = true) { client ->
            val user = performRequest {
                client.user.getUserDetails(fields = UserField.entries)
            }

            assertNotNull(user)
            assertTrue(user.id > 0)
            assertTrue(user.name.isNotBlank())
            assertNotNull(user.animeStatistics)
        }
}
