package io.github.rribeiro5.koani

import io.github.rribeiro5.koani.di.KoaniContainer

public class KoaniClient internal constructor(private val container: KoaniContainer) {

    init {
        container.logger.d { "KoaniClient successfully initialized" }
    }

    public class Builder(internal val clientId: String) {
        internal var timeoutMillis: Long? = null
        internal var logLevel: LogLevel = LogLevel.NONE

        public fun timeoutMillis(timeoutMillis: Long): Builder = apply {
            this.timeoutMillis = timeoutMillis
        }

        public fun logLevel(logLevel: LogLevel): Builder = apply {
            this.logLevel = logLevel
        }

        public fun build(): KoaniClient {
            val container = KoaniContainer(
                clientId = clientId,
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
