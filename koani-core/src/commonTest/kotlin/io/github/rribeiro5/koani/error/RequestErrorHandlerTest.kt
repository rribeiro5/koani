package io.github.rribeiro5.koani.error

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RequestErrorHandlerTest {

    private fun createMockClient(
        status: HttpStatusCode,
        content: String = ""
    ): HttpClient {
        val engine = MockEngine { _ ->
            respond(
                content = content,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        return HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) {
                json()
            }
        }
    }

    @Test
    fun `handleRequestException should throw BadRequestException for 400`() = runTest {
        val client = createMockClient(
            status = HttpStatusCode.BadRequest,
            content = """{"error":"bad_request","message":"Invalid parameters"}"""
        )

        val cause = assertFailsWith<ResponseException> { client.get("/") }

        val exception = assertFailsWith<BadRequestException> {
            handleRequestException(cause)
        }
        assertEquals("bad_request", exception.error)
        assertEquals("Invalid parameters", exception.apiMessage)
        assertEquals("Invalid parameters", exception.message)
    }

    @Test
    fun `handleRequestException should throw UnauthorizedException for 401`() = runTest {
        val client = createMockClient(
            status = HttpStatusCode.Unauthorized,
            content = """{"error":"unauthorized","message":"No token"}"""
        )

        val cause = assertFailsWith<ResponseException> { client.get("/") }

        val exception = assertFailsWith<UnauthorizedException> {
            handleRequestException(cause)
        }
        assertEquals("unauthorized", exception.error)
        assertEquals("No token", exception.apiMessage)
        assertEquals("No token", exception.message)
    }

    @Test
    fun `handleRequestException should throw ForbiddenException for 403`() = runTest {
        val client = createMockClient(
            status = HttpStatusCode.Forbidden,
            content = """{"error":"forbidden","message":"Access denied"}"""
        )

        val cause = assertFailsWith<ResponseException> { client.get("/") }

        val exception = assertFailsWith<ForbiddenException> {
            handleRequestException(cause)
        }
        assertEquals("forbidden", exception.error)
        assertEquals("Access denied", exception.apiMessage)
        assertEquals("Access denied", exception.message)
    }

    @Test
    fun `handleRequestException should throw NotFoundException for 404`() = runTest {
        val client = createMockClient(
            status = HttpStatusCode.NotFound,
            content = """{"error":"not_found","message":"Resource not found"}"""
        )

        val cause = assertFailsWith<ResponseException> { client.get("/") }

        val exception = assertFailsWith<NotFoundException> {
            handleRequestException(cause)
        }
        assertEquals("not_found", exception.error)
        assertEquals("Resource not found", exception.apiMessage)
        assertEquals("Resource not found", exception.message)
    }

    @Test
    fun `handleRequestException should throw RedirectResponseException for 3xx`() = runTest {
        val client = createMockClient(status = HttpStatusCode.PermanentRedirect)

        val cause = assertFailsWith<ResponseException> { client.get("/") }

        val exception = assertFailsWith<RedirectResponseException> {
            handleRequestException(cause)
        }
        assertEquals(308, exception.statusCode)
    }

    @Test
    fun `handleRequestException should throw ServerResponseException for 5xx`() = runTest {
        val client = createMockClient(status = HttpStatusCode.InternalServerError)

        val cause = assertFailsWith<ResponseException> { client.get("/") }

        val exception = assertFailsWith<ServerResponseException> {
            handleRequestException(cause)
        }
        assertEquals(500, exception.statusCode)
    }

    @Test
    fun `handleRequestException should rethrow if not ResponseException`() = runTest {
        val otherException = IllegalArgumentException("Test")

        val exception = assertFailsWith<IllegalArgumentException> {
            handleRequestException(otherException)
        }
        assertEquals("Test", exception.message)
    }

    @Test
    fun `handleRequestException should handle null error body gracefully`() = runTest {
        val client = createMockClient(
            status = HttpStatusCode.BadRequest,
            content = ""
        )

        val cause = assertFailsWith<ResponseException> { client.get("/") }

        val exception = assertFailsWith<BadRequestException> {
            handleRequestException(cause)
        }
        assertEquals(null, exception.error)
        assertEquals(null, exception.apiMessage)
        assertEquals("Bad Request", exception.message)
    }
}
