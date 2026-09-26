package io.github.rribeiro5.koani.error.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorResponse(
    val error: String? = null,
    val message: String? = null
)
