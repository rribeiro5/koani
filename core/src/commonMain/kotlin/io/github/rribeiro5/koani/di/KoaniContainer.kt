package io.github.rribeiro5.koani.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class KoaniContainer(
    clientId: String,
    timeoutMillis: Long? = null,
    engine: HttpClientEngine = getEngine(),
) {

    companion object {
        private const val BASE_URL = "https://api.myanimelist.net/"
        private const val CLIENT_ID_HEADER = "X-MAL-CLIENT-ID"
    }

    private val httpClient: HttpClient = HttpClient(engine) {
        expectSuccess = true
        defaultRequest {
            url(BASE_URL)
            header(CLIENT_ID_HEADER, clientId)
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
    }
}
