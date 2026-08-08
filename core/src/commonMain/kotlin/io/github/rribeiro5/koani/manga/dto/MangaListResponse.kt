package io.github.rribeiro5.koani.manga.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MangaListEdgeResponse(
    @SerialName("node")
    val node: MangaResponse
)

@Serializable
internal data class MangaRankingEdgeResponse(
    @SerialName("node")
    val node: MangaResponse,
    @SerialName("ranking")
    val ranking: MangaRankingResponse
)

@Serializable
internal data class MangaRankingResponse(
    @SerialName("rank")
    val rank: Int,
    @SerialName("previous_rank")
    val previousRank: Int? = null
)
