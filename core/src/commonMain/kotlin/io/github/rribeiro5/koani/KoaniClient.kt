package io.github.rribeiro5.koani

import io.github.rribeiro5.koani.di.KoaniContainer

public class KoaniClient internal constructor(private val container: KoaniContainer) {

    public class Builder(private val clientId: String) {
        private var timeoutMillis: Long? = null

        public fun timeoutMillis(timeoutMillis: Long): Builder = apply {
            this.timeoutMillis = timeoutMillis
        }

        public fun build(): KoaniClient {
            val container = KoaniContainer(
                clientId = clientId,
                timeoutMillis = timeoutMillis
            )
            return KoaniClient(container)
        }
    }
}
