package io.github.rribeiro5.koani.auth.service

import io.github.rribeiro5.koani.auth.dto.TokenResponses
import io.github.rribeiro5.koani.di.KtorRequestMock
import io.github.rribeiro5.koani.http.fakeHttpClient
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorAuthServiceTest {

    private fun createSubject(requestHandler: KtorRequestMock) = KtorAuthService(
        httpClient = fakeHttpClient(requestHandler = requestHandler)
    )

    @Test
    fun `authenticate should return TokenResponse on success`() = runTest {
        val subject = createSubject {
            respond(
                content = TokenResponses.SUCCESS,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val response = subject.authenticate(
            clientId = "client-id",
            authorizationCode = "code",
            codeVerifier = "verifier"
        )

        assertEquals("access-token-123", response.accessToken)
        assertEquals("refresh-token-456", response.refreshToken)
        assertEquals(3600, response.expiresIn)
    }

    @Test
    fun `refreshTokens should return TokenResponse on success`() = runTest {
        val subject = createSubject {
            respond(
                content = TokenResponses.SUCCESS,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val response = subject.refreshTokens(
            clientId = "client-id",
            refreshToken = "old-refresh-token"
        )

        assertEquals("access-token-123", response.accessToken)
        assertEquals("refresh-token-456", response.refreshToken)
        assertEquals(3600, response.expiresIn)
    }
}
