package io.github.rribeiro5.koani.auth.persistence.ksafe

import eu.anifantakis.lib.ksafe.KSafe
import io.github.rribeiro5.koani.ExperimentalKoaniApi
import io.github.rribeiro5.koani.auth.TokenManager

/**
 * [TokenManager] implementation using [KSafe] for secure persistence.
 */
@ExperimentalKoaniApi
public class KSafeTokenManager(
    private val kSafe: KSafe
) : TokenManager {
    public override fun accessToken(): String? = kSafe.getDirect(ACCESS_TOKEN_KEY, null)

    public override fun refreshToken(): String? = kSafe.getDirect(REFRESH_TOKEN_KEY, null)

    public override fun storeTokens(accessToken: String, refreshToken: String?) {
        kSafe.putDirect(ACCESS_TOKEN_KEY, accessToken)
        if (refreshToken != null) {
            kSafe.putDirect(REFRESH_TOKEN_KEY, refreshToken)
        } else {
            kSafe.deleteDirect(REFRESH_TOKEN_KEY)
        }
    }

    public override fun clearTokens() {
        kSafe.deleteDirect(ACCESS_TOKEN_KEY)
        kSafe.deleteDirect(REFRESH_TOKEN_KEY)
    }

    private companion object {
        private const val ACCESS_TOKEN_KEY: String = "access_token"
        private const val REFRESH_TOKEN_KEY: String = "refresh_token"
    }
}
