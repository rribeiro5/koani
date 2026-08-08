package io.github.rribeiro5.koani.manga.service

import io.github.rribeiro5.koani.core.dto.PaginatedListResponse
import io.github.rribeiro5.koani.manga.dto.MangaRankingEdgeResponse
import io.github.rribeiro5.koani.manga.dto.MangaResponse

internal interface MangaService {
    suspend fun getMangaList(
        query: String? = null,
        limit: Int? = null,
        offset: Int? = null,
        fields: List<String>? = null,
    ): PaginatedListResponse<MangaResponse>

    suspend fun getMangaDetails(
        mangaId: Int,
        fields: List<String>? = null,
    ): MangaResponse

    suspend fun getMangaRanking(
        rankingType: String,
        limit: Int? = null,
        offset: Int? = null,
        fields: List<String>? = null,
    ): PaginatedListResponse<MangaRankingEdgeResponse>
}
