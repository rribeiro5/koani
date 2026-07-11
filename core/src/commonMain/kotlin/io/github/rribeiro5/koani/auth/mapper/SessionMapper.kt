package io.github.rribeiro5.koani.auth.mapper

import io.github.rribeiro5.koani.auth.Session
import io.github.rribeiro5.koani.auth.dto.TokenResponse

internal fun TokenResponse.toSession(): Session = Session(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn,
)
