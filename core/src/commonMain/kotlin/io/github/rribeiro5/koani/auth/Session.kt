package io.github.rribeiro5.koani.auth

/**
 * Represents an authentication session with tokens.
 *
 * @property accessToken The access token used for authenticated requests.
 * @property refreshToken The refresh token used to obtain new access tokens.
 * @property expiresIn The duration in seconds until the access token expires.
 */
public data class Session(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
)
