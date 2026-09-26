package io.github.rribeiro5.koani.auth.persistence.ksafe

import eu.anifantakis.lib.ksafe.KSafe
import io.github.rribeiro5.koani.ExperimentalKoaniApi
import io.github.rribeiro5.koani.auth.TokenManager

/**
 * A [TokenManager] implementation that uses [KSafe] for secure persistence.
 *
 * This implementation stores tokens securely on the device using the platform's
 * preferred secure storage (e.g., EncryptedSharedPreferences on Android).
 *
 * @property kSafe The [KSafe] instance used for storage operations.
 */
@ExperimentalKoaniApi
public class KSafeTokenManager(
    private val kSafe: KSafe
) : TokenManager {
    /**
     * Retrieves the current access token from secure storage.
     *
     * @return The access token, or `null` if not found.
     */
    public override fun accessToken(): String? = kSafe.getDirect(ACCESS_TOKEN_KEY, null)

    /**
     * Retrieves the current refresh token from secure storage.
     *
     * @return The refresh token, or `null` if not found.
     */
    public override fun refreshToken(): String? = kSafe.getDirect(REFRESH_TOKEN_KEY, null)

    /**
     * Securely stores the access and refresh tokens.
     *
     * @param accessToken The access token to store.
     * @param refreshToken The refresh token to store. If `null`, any existing refresh token is deleted.
     */
    public override fun storeTokens(accessToken: String, refreshToken: String?) {
        kSafe.putDirect(ACCESS_TOKEN_KEY, accessToken)
        if (refreshToken != null) {
            kSafe.putDirect(REFRESH_TOKEN_KEY, refreshToken)
        } else {
            kSafe.deleteDirect(REFRESH_TOKEN_KEY)
        }
    }

    /**
     * Deletes all stored tokens from secure storage.
     */
    public override fun clearTokens() {
        kSafe.deleteDirect(ACCESS_TOKEN_KEY)
        kSafe.deleteDirect(REFRESH_TOKEN_KEY)
    }

    private companion object {
        private const val ACCESS_TOKEN_KEY: String = "access_token"
        private const val REFRESH_TOKEN_KEY: String = "refresh_token"
    }
}
