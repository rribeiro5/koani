package io.github.rribeiro5.koani.error

import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorDto(
    val error: String? = null,
    val message: String? = null
)
