package io.github.rribeiro5.koani.di

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import io.github.rribeiro5.koani.LogLevel
import io.github.rribeiro5.koani.anime.service.AnimeService
import io.github.rribeiro5.koani.anime.service.KtorAnimeService
import io.github.rribeiro5.koani.manga.service.KtorMangaService
import io.github.rribeiro5.koani.manga.service.MangaService
import io.github.rribeiro5.koani.auth.MemoryTokenManager
import io.github.rribeiro5.koani.auth.TokenManager
import io.github.rribeiro5.koani.auth.service.AuthService
import io.github.rribeiro5.koani.auth.service.KtorAuthService
import io.github.rribeiro5.koani.http.buildHttpClient
import io.github.rribeiro5.koani.http.getEngine
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine

internal class KoaniContainer(
    val clientId: String,
    val clientSecret: String? = null,
    val tokenManager: TokenManager = MemoryTokenManager(),
    timeoutMillis: Long? = null,
    logLevel: LogLevel = LogLevel.NONE,
    engine: HttpClientEngine = getEngine(),
    logWriter: LogWriter = platformLogWriter(),
) {

    companion object {
        private const val LOGGER_TAG = "KoaniClient"
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

    private val authHttpClient: HttpClient = buildHttpClient(
        engine = engine,
        clientId = clientId,
        clientSecret = clientSecret,
        tokenManager = tokenManager,
        authService = null,
        logger = logger,
        timeoutMillis = timeoutMillis,
        logLevel = logLevel,
    )

    val authService: AuthService = KtorAuthService(authHttpClient)

    private val httpClient: HttpClient = buildHttpClient(
        engine = engine,
        clientId = clientId,
        clientSecret = clientSecret,
        tokenManager = tokenManager,
        authService = authService,
        logger = logger,
        timeoutMillis = timeoutMillis,
        logLevel = logLevel,
    )

    val animeService: AnimeService = KtorAnimeService(httpClient)

    val mangaService: MangaService = KtorMangaService(httpClient)
}
