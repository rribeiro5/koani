package io.github.rribeiro5.koani.di

import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.LogWriter
import io.github.rribeiro5.koani.LogLevel
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData

internal typealias KtorRequestMock = suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData

@OptIn(ExperimentalKermitApi::class)
internal fun fakeContainer(
    clientId: String = "123456789",
    timeoutMillis: Long? = null,
    logLevel: LogLevel = LogLevel.NONE,
    requestHandler: KtorRequestMock = { respondOk() },
    logWriter: LogWriter = fakeLogWriter(),
): KoaniContainer = KoaniContainer(
    clientId = clientId,
    timeoutMillis = timeoutMillis,
    logLevel = logLevel,
    engine = MockEngine(requestHandler),
    logWriter = logWriter,
)
