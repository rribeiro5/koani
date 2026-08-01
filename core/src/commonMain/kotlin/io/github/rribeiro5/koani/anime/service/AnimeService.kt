package io.github.rribeiro5.koani.anime.service

import io.github.rribeiro5.koani.anime.dto.AnimeResponse
import io.github.rribeiro5.koani.core.dto.PaginatedListResponse

internal interface AnimeService {
    suspend fun getAnimeList(
        query: String? = null,
        limit: Int? = null,
        offset: Int? = null,
        fields: List<String>? = null,
    ): PaginatedListResponse<AnimeResponse>

    suspend fun getAnimeDetails(
        animeId: Int,
        fields: List<String>? = null,
    ): AnimeResponse
}
