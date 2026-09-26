package io.github.rribeiro5.koani.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MemoryTokenManagerTest {

    private fun createSubject() = MemoryTokenManager()

    @Test
    fun `should initialize with null tokens`() {
        val manager = createSubject()
        assertNull(manager.accessToken())
        assertNull(manager.refreshToken())
    }

    @Test
    fun `should store and retrieve tokens`() {
        val manager = createSubject()
        val accessToken = "access_token"
        val refreshToken = "refresh_token"

        manager.storeTokens(accessToken, refreshToken)

        assertEquals(accessToken, manager.accessToken())
        assertEquals(refreshToken, manager.refreshToken())
    }

    @Test
    fun `should clear tokens`() {
        val manager = createSubject()
        manager.storeTokens("access", "refresh")

        manager.clearTokens()

        assertNull(manager.accessToken())
        assertNull(manager.refreshToken())
    }

    @Test
    fun `should update tokens`() {
        val manager = createSubject()
        manager.storeTokens("old_access", "old_refresh")

        val newAccess = "new_access"
        val newRefresh = "new_refresh"
        manager.storeTokens(newAccess, newRefresh)

        assertEquals(newAccess, manager.accessToken())
        assertEquals(newRefresh, manager.refreshToken())
    }
}
