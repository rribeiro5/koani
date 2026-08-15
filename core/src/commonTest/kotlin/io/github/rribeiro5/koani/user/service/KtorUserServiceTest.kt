package io.github.rribeiro5.koani.user.service

import io.github.rribeiro5.koani.di.KtorRequestMock
import io.github.rribeiro5.koani.http.fakeHttpClient
import io.github.rribeiro5.koani.user.dto.UserResponses
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorUserServiceTest {

    private fun createSubject(requestHandler: KtorRequestMock) = KtorUserService(
        httpClient = fakeHttpClient(requestHandler = requestHandler)
    )

    @Test
    fun `getUserDetails should return user details`() = runTest {
        val subject = createSubject {
            respond(
                content = UserResponses.USER_DETAILS,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getUserDetails(userName = "@me")

        assertEquals(1, result.id)
        assertEquals("testuser", result.name)
        assertEquals("male", result.gender)
    }
}
