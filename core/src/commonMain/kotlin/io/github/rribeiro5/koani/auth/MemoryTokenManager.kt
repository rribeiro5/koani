package io.github.rribeiro5.koani.auth

import kotlin.concurrent.Volatile

/**
 * An in-memory implementation of [TokenManager] that is thread-safe.
 */
public class MemoryTokenManager : TokenManager {
    private data class TokenState(val accessToken: String?, val refreshToken: String?)

    @Volatile
    private var state = TokenState(null, null)

    override fun accessToken(): String? = state.accessToken

    override fun refreshToken(): String? = state.refreshToken

    override fun storeTokens(accessToken: String, refreshToken: String?) {
        state = TokenState(accessToken, refreshToken)
    }

    override fun clearTokens() {
        state = TokenState(null, null)
    }
}
