package io.github.rribeiro5.koani.manga.dto

import io.github.rribeiro5.koani.core.dto.RankingResponse
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
    val ranking: RankingResponse
)

@Serializable
internal data class UserMangaListEdgeResponse(
    @SerialName("node")
    val node: MangaResponse,
    @SerialName("list_status")
    val listStatus: UserMangaListStatusResponse? = null
)
