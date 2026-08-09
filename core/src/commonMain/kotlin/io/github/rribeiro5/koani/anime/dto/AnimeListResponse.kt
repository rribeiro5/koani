package io.github.rribeiro5.koani.anime.dto

import io.github.rribeiro5.koani.core.dto.RankingResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AnimeListEdgeResponse(
    @SerialName("node")
    val node: AnimeResponse
)

@Serializable
internal data class AnimeRankingEdgeResponse(
    @SerialName("node")
    val node: AnimeResponse,
    @SerialName("ranking")
    val ranking: RankingResponse
)

@Serializable
internal data class UserAnimeListEdgeResponse(
    @SerialName("node")
    val node: AnimeResponse,
    @SerialName("list_status")
    val listStatus: UserAnimeListStatusResponse
)
