package io.github.rribeiro5.koani.auth.service

import io.github.rribeiro5.koani.auth.dto.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters

internal class KtorAuthService(
    private val httpClient: HttpClient,
) : AuthService {

    companion object {
        private const val TOKEN_URL = "https://myanimelist.net/v1/oauth2/token"
        private const val GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code"
        private const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
    }

    override suspend fun authenticate(
        clientId: String,
        authorizationCode: String,
        codeVerifier: String,
        clientSecret: String?,
        redirectUri: String?,
    ): TokenResponse {
        return httpClient.submitForm(
            url = TOKEN_URL,
            formParameters = parameters {
                append("client_id", clientId)
                append("grant_type", GRANT_TYPE_AUTHORIZATION_CODE)
                append("code", authorizationCode)
                append("code_verifier", codeVerifier)
                clientSecret?.let { append("client_secret", it) }
                redirectUri?.let { append("redirect_uri", it) }
            }
        ).body()
    }

    override suspend fun refreshTokens(
        clientId: String,
        refreshToken: String,
        clientSecret: String?,
    ): TokenResponse {
        return httpClient.submitForm(
            url = TOKEN_URL,
            formParameters = parameters {
                append("client_id", clientId)
                append("grant_type", GRANT_TYPE_REFRESH_TOKEN)
                append("refresh_token", refreshToken)
                clientSecret?.let { append("client_secret", it) }
            }
        ).body()
    }
}
