package io.github.rribeiro5.koani.auth

import kotlin.concurrent.Volatile

/**
 * A thread-safe, in-memory implementation of [TokenManager].
 *
 * This implementation stores tokens in memory only. Tokens will be lost
 * when the application process is terminated.
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
