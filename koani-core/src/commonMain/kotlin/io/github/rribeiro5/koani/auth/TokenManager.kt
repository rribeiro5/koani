package io.github.rribeiro5.koani.auth

/**
 * Interface for managing authentication tokens.
 *
 * Implementations of this interface are responsible for storing and retrieving
 * access and refresh tokens used for authenticated requests to the MyAnimeList API.
 */
public interface TokenManager {
    /**
     * Retrieves the current access token.
     *
     * @return The current access token, or `null` if no token is stored.
     */
    public fun accessToken(): String?

    /**
     * Retrieves the current refresh token.
     *
     * @return The current refresh token, or `null` if no token is stored.
     */
    public fun refreshToken(): String?

    /**
     * Stores the new access and refresh tokens.
     *
     * @param accessToken The new access token to store.
     * @param refreshToken The new refresh token to store, or `null` if not provided.
     */
    public fun storeTokens(accessToken: String, refreshToken: String?)

    /**
     * Clears all stored tokens, effectively logging out the user.
     */
    public fun clearTokens()
}
