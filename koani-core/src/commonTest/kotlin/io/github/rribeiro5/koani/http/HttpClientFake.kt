package io.github.rribeiro5.koani.http

import co.touchlab.kermit.Logger
import io.github.rribeiro5.koani.LogLevel
import io.github.rribeiro5.koani.auth.MemoryTokenManager
import io.github.rribeiro5.koani.auth.TokenManager
import io.github.rribeiro5.koani.di.KtorRequestMock
import io.github.rribeiro5.koani.di.fakeLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk

internal fun fakeHttpClient(
    requestHandler: KtorRequestMock = { respondOk() },
    clientId: String = "123456789",
    tokenManager: TokenManager = MemoryTokenManager(),
    timeoutMillis: Long? = null,
    logger: Logger = fakeLogger(),
    logLevel: LogLevel = LogLevel.NONE,
): HttpClient = buildHttpClient(
    engine = MockEngine(requestHandler),
    clientId = clientId,
    tokenManager = tokenManager,
    logger = logger,
    timeoutMillis = timeoutMillis,
    logLevel = logLevel,
)
