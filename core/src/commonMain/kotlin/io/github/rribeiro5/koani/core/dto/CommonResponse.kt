package io.github.rribeiro5.koani.core.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PictureResponse(
    @SerialName("medium")
    val medium: String,
    @SerialName("large")
    val large: String? = null,
)

@Serializable
internal data class AlternativeTitlesResponse(
    @SerialName("synonyms")
    val synonyms: List<String> = emptyList(),
    @SerialName("en")
    val en: String? = null,
    @SerialName("ja")
    val ja: String? = null,
)

@Serializable
internal data class GenreResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)
