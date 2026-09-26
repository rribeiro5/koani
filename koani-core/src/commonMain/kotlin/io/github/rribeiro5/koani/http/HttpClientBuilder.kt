package io.github.rribeiro5.koani.http

import co.touchlab.kermit.Logger
import io.github.rribeiro5.koani.LogLevel
import io.github.rribeiro5.koani.auth.TokenManager
import io.github.rribeiro5.koani.auth.service.AuthService
import io.github.rribeiro5.koani.error.handleRequestException
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.CancellationException
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.LogLevel as KtorLogLevel
import io.ktor.client.plugins.logging.Logger as KtorLogger

private const val BASE_URL = "https://api.myanimelist.net/"
private const val CLIENT_ID_HEADER = "X-MAL-CLIENT-ID"

internal fun buildHttpClient(
    engine: HttpClientEngine,
    clientId: String,
    clientSecret: String? = null,
    tokenManager: TokenManager,
    authService: AuthService? = null,
    logger: Logger,
    timeoutMillis: Long?,
    logLevel: LogLevel,
): HttpClient = HttpClient(engine) {
    expectSuccess = true
    defaultRequest {
        url(BASE_URL)
        header(CLIENT_ID_HEADER, clientId)
    }
    if (authService != null) {
        install(Auth) {
            bearer {
                cacheTokens = false
                loadTokens {
                    val accessToken = tokenManager.accessToken() ?: return@loadTokens null
                    BearerTokens(accessToken, tokenManager.refreshToken())
                }
                refreshTokens {
                    val refreshToken = oldTokens?.refreshToken ?: return@refreshTokens null
                    try {
                        val response = authService.refreshTokens(
                            clientId = clientId,
                            refreshToken = refreshToken,
                            clientSecret = clientSecret,
                        )
                        tokenManager.storeTokens(response.accessToken, response.refreshToken)
                        BearerTokens(response.accessToken, response.refreshToken)
                    } catch (e: CancellationException) {
                        throw e
                    } catch (_: Exception) {
                        null
                    }
                }
            }
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
        connectTimeoutMillis = timeoutMillis
        socketTimeoutMillis = timeoutMillis
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
