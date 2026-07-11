package io.github.rribeiro5.koani.http

import co.touchlab.kermit.Logger
import io.github.rribeiro5.koani.LogLevel
import io.github.rribeiro5.koani.auth.TokenManager
import io.github.rribeiro5.koani.error.handleRequestException
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.LogLevel as KtorLogLevel
import io.ktor.client.plugins.logging.Logger as KtorLogger

private const val BASE_URL = "https://api.myanimelist.net/"
private const val CLIENT_ID_HEADER = "X-MAL-CLIENT-ID"

internal fun buildHttpClient(
    engine: HttpClientEngine,
    clientId: String,
    tokenManager: TokenManager,
    logger: Logger,
    timeoutMillis: Long?,
    logLevel: LogLevel,
): HttpClient = HttpClient(engine) {
    expectSuccess = true
    defaultRequest {
        url(BASE_URL)
        header(CLIENT_ID_HEADER, clientId)
        tokenManager.accessToken()?.let { token ->
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            }
        )
    }
    HttpResponseValidator {
        handleResponseException { cause ->
            handleRequestException(cause)
        }
    }
    install(HttpTimeout) {
        requestTimeoutMillis = timeoutMillis
    }

    if (logLevel != LogLevel.NONE) {
        install(Logging) {
            this.logger = object : KtorLogger {
                override fun log(message: String) {
                    logger.v { message }
                }
            }
            level = KtorLogLevel.ALL
            sanitizeHeader { header ->
                header == HttpHeaders.Authorization || header == CLIENT_ID_HEADER
            }
        }
    }
}
