package io.github.rribeiro5.koani.auth

/**
 * Interface for managing authentication tokens.
 */
public interface TokenManager {
    /**
     * Returns the current access token, or null if not available.
     */
    public fun accessToken(): String?

    /**
     * Returns the current refresh token, or null if not available.
     */
    public fun refreshToken(): String?

    /**
     * Stores the new access and refresh tokens.
     */
    public fun storeTokens(accessToken: String, refreshToken: String?)

    /**
     * Clears all stored tokens.
     */
    public fun clearTokens()
}
