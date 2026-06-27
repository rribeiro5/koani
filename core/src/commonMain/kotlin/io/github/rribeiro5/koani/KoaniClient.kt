package io.github.rribeiro5.koani

import io.github.rribeiro5.koani.auth.MemoryTokenManager
import io.github.rribeiro5.koani.auth.TokenManager
import io.github.rribeiro5.koani.di.KoaniContainer

public class KoaniClient internal constructor(private val container: KoaniContainer) {

    init {
        require(container.clientId.isNotBlank()) {
            "Client ID cannot be empty".also { container.logger.e(it) }
        }
        container.logger.d { "KoaniClient successfully initialized" }
    }

    public class Builder(internal val clientId: String) {
        internal var timeoutMillis: Long? = null
        internal var logLevel: LogLevel = LogLevel.NONE
        internal var tokenManager: TokenManager = MemoryTokenManager()

        public fun timeoutMillis(timeoutMillis: Long): Builder = apply {
            this.timeoutMillis = timeoutMillis
        }

        public fun logLevel(logLevel: LogLevel): Builder = apply {
            this.logLevel = logLevel
        }

        public fun tokenManager(tokenManager: TokenManager): Builder = apply {
            this.tokenManager = tokenManager
        }

        public fun build(): KoaniClient {
            val container = KoaniContainer(
                clientId = clientId,
                tokenManager = tokenManager,
                timeoutMillis = timeoutMillis,
                logLevel = logLevel,
            )

            container.logger.d { "Initializing KoaniClient" }
            container.logger.v {
                val sanitizedClientId = if (clientId.length > 4) {
                    clientId.take(4) + "*".repeat(clientId.length - 4)
                } else {
                    "*".repeat(clientId.length)
                }
                "Initializing KoaniClient (clientId=$sanitizedClientId, timeoutMillis=$timeoutMillis, logLevel=$logLevel)"
            }

            return KoaniClient(container)
        }
    }
}
