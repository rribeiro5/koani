package io.github.rribeiro5.koani.auth.service

import io.github.rribeiro5.koani.auth.dto.TokenResponse

internal interface AuthService {
    suspend fun authenticate(
        clientId: String,
        authorizationCode: String,
        codeVerifier: String,
        clientSecret: String? = null,
        redirectUri: String? = null,
    ): TokenResponse

    suspend fun refreshTokens(
        clientId: String,
        refreshToken: String,
        clientSecret: String? = null,
    ): TokenResponse
}
