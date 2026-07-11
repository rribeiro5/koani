package io.github.rribeiro5.koani.error.dto

internal object ErrorResponses {
    const val INVALID_GRANT = """
        {
            "error": "invalid_grant",
            "message": "The provided authorization grant is invalid."
        }
    """
}
