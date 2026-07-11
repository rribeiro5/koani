package io.github.rribeiro5.koani.auth.mapper

import io.github.rribeiro5.koani.auth.dto.TokenResponse
import kotlin.test.Test
import kotlin.test.assertEquals

class SessionMapperTest {

    @Test
    fun `toSession should map all fields correctly`() {
        val tokenResponse = TokenResponse(
            tokenType = "Bearer",
            expiresIn = 3600,
            accessToken = "access-token",
            refreshToken = "refresh-token"
        )

        val session = tokenResponse.toSession()

        assertEquals(tokenResponse.accessToken, session.accessToken)
        assertEquals(tokenResponse.refreshToken, session.refreshToken)
        assertEquals(tokenResponse.expiresIn, session.expiresIn)
    }
}
