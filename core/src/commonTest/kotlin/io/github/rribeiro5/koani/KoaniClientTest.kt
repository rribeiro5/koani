package io.github.rribeiro5.koani

import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Severity
import io.github.rribeiro5.koani.auth.MemoryTokenManager
import io.github.rribeiro5.koani.auth.dto.TokenResponses
import io.github.rribeiro5.koani.di.KoaniContainer
import io.github.rribeiro5.koani.di.assertContains
import io.github.rribeiro5.koani.di.fakeContainer
import io.github.rribeiro5.koani.di.fakeLogWriter
import io.github.rribeiro5.koani.error.BadRequestException
import io.github.rribeiro5.koani.error.UnauthorizedException
import io.github.rribeiro5.koani.error.dto.ErrorResponses
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalKermitApi::class)
class KoaniClientTest {

    private fun createSubject(container: KoaniContainer): KoaniClient {
        return KoaniClient(container)
    }

    @Test
    fun `init should log successfully initialized`() {
        val logWriter = fakeLogWriter()
        val container = fakeContainer(
            logLevel = LogLevel.DEBUG,
            logWriter = logWriter,
        )

        createSubject(container)

        logWriter.assertContains {
            message == "KoaniClient successfully initialized"
        }
    }

    @Test
    fun `init should throw exception and log when clientId is blank`() {
        val logWriter = fakeLogWriter()
        val container = fakeContainer(
            clientId = " ",
            logWriter = logWriter,
            logLevel = LogLevel.ERROR
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            createSubject(container)
        }

        assertEquals("Client ID cannot be empty", exception.message)
        logWriter.assertContains {
            message == "Client ID cannot be empty" && severity == Severity.Error
        }
    }

    // region Auth tests
    @Test
    fun `authenticate should store tokens and return session on success`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = TokenResponses.SUCCESS,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val session = subject.auth.authenticate("code", "verifier")

        assertEquals("access-token-123", session.accessToken)
        assertEquals("refresh-token-456", session.refreshToken)
        assertEquals("access-token-123", subject.auth.tokenManager.accessToken())
        assertEquals("refresh-token-456", subject.auth.tokenManager.refreshToken())
    }

    @Test
    fun `authenticate should throw BadRequestException on 400 error`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = ErrorResponses.INVALID_GRANT,
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val exception = assertFailsWith<BadRequestException> {
            subject.auth.authenticate("invalid-code", "verifier")
        }

        assertEquals("invalid_grant", exception.error)
        assertEquals(
            "The provided authorization grant is invalid.",
            exception.apiMessage
        )
    }

    @Test
    fun `refreshTokens should store tokens and return session on success`() = runTest {
        val tokenManager = MemoryTokenManager().apply {
            storeTokens("old-access", "old-refresh")
        }
        val container = fakeContainer(
            tokenManager = tokenManager,
            requestHandler = {
                respond(
                    content = TokenResponses.SUCCESS,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val session = subject.auth.refreshTokens()

        assertEquals("access-token-123", session.accessToken)
        assertEquals("refresh-token-456", session.refreshToken)
        assertEquals("access-token-123", subject.auth.tokenManager.accessToken())
        assertEquals("refresh-token-456", subject.auth.tokenManager.refreshToken())
    }

    @Test
    fun `refreshTokens should throw UnauthorizedException on 401 error`() = runTest {
        val tokenManager = MemoryTokenManager().apply {
            storeTokens("old-access", "old-refresh")
        }
        val container = fakeContainer(
            tokenManager = tokenManager,
            requestHandler = {
                respond(
                    content = ErrorResponses.INVALID_GRANT,
                    status = HttpStatusCode.Unauthorized,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val exception = assertFailsWith<UnauthorizedException> {
            subject.auth.refreshTokens()
        }

        assertEquals("invalid_grant", exception.error)
    }

    @Test
    fun `refreshTokens should throw IllegalStateException when no token available`() = runTest {
        val container = fakeContainer()
        val subject = createSubject(container)

        val exception = assertFailsWith<IllegalStateException> {
            subject.auth.refreshTokens(refreshToken = null)
        }

        assertEquals("No refresh token available", exception.message)
    }

    @Test
    fun `logout should clear tokens`() {
        val tokenManager = MemoryTokenManager().apply {
            storeTokens("access", "refresh")
        }
        val container = fakeContainer(tokenManager = tokenManager)
        val subject = createSubject(container)

        subject.auth.logout()

        assertEquals(null, subject.auth.tokenManager.accessToken())
        assertEquals(null, subject.auth.tokenManager.refreshToken())
    }
    // endregion
}
