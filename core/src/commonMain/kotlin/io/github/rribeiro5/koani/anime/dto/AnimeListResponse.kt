package io.github.rribeiro5.koani.anime.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AnimeListEdgeResponse(
    @SerialName("node")
    val node: AnimeResponse
)
