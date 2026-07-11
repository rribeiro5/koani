package io.github.rribeiro5.koani.auth.dto

internal object TokenResponses {
    const val SUCCESS = """
        {
            "token_type": "Bearer",
            "expires_in": 3600,
            "access_token": "access-token-123",
            "refresh_token": "refresh-token-456"
        }
    """

    const val ERROR = """
        {
            "error": "invalid_grant",
            "message": "The provided authorization grant is invalid."
        }
    """
}
