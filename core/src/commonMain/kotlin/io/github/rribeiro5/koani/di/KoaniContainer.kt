package io.github.rribeiro5.koani.di

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import io.github.rribeiro5.koani.LogLevel
import io.github.rribeiro5.koani.auth.MemoryTokenManager
import io.github.rribeiro5.koani.auth.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
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

internal class KoaniContainer(
    val clientId: String,
    val tokenManager: TokenManager = MemoryTokenManager(),
    timeoutMillis: Long? = null,
    logLevel: LogLevel = LogLevel.NONE,
    engine: HttpClientEngine = getEngine(),
    logWriter: LogWriter = platformLogWriter(),
) {

    companion object {
        private const val LOGGER_TAG = "KoaniClient"

        private const val BASE_URL = "https://api.myanimelist.net/"
        private const val CLIENT_ID_HEADER = "X-MAL-CLIENT-ID"
    }

    val logger = Logger(
        config = loggerConfigInit(
            logWriter,
            minSeverity = when (logLevel) {
                LogLevel.VERBOSE -> Severity.Verbose
                LogLevel.DEBUG -> Severity.Debug
                LogLevel.INFO -> Severity.Info
                LogLevel.WARN -> Severity.Warn
                LogLevel.ERROR -> Severity.Error
                LogLevel.ASSERT -> Severity.Assert
                LogLevel.NONE -> Severity.Assert
            }
        ),
        tag = LOGGER_TAG
    )

    private val httpClient: HttpClient = HttpClient(engine) {
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
        install(HttpTimeout) {
            requestTimeoutMillis = timeoutMillis
        }

        if (logLevel != LogLevel.NONE) {
            install(Logging) {
                logger = object : KtorLogger {
                    override fun log(message: String) {
                        this@KoaniContainer.logger.v { message }
                    }
                }
                level = KtorLogLevel.ALL
                sanitizeHeader { header -> 
                    header == HttpHeaders.Authorization || header == CLIENT_ID_HEADER 
                }
            }
        }
    }
}
