package io.github.rribeiro5.koani.anime.service

import io.github.rribeiro5.koani.anime.dto.AnimeRankingEdgeResponse
import io.github.rribeiro5.koani.anime.dto.AnimeResponse
import io.github.rribeiro5.koani.anime.dto.UserAnimeListEdgeResponse
import io.github.rribeiro5.koani.anime.dto.UserAnimeListStatusResponse
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

    suspend fun getAnimeRanking(
        rankingType: String,
        limit: Int? = null,
        offset: Int? = null,
        fields: List<String>? = null,
    ): PaginatedListResponse<AnimeRankingEdgeResponse>

    suspend fun getSeasonalAnimes(
        year: Int,
        season: String,
        sort: String? = null,
        limit: Int? = null,
        offset: Int? = null,
        fields: List<String>? = null,
    ): PaginatedListResponse<AnimeResponse>

    suspend fun getSuggestedAnimes(
        limit: Int? = null,
        offset: Int? = null,
        fields: List<String>? = null,
    ): PaginatedListResponse<AnimeResponse>

    suspend fun getUserAnimeList(
        userName: String,
        status: String? = null,
        sort: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): PaginatedListResponse<UserAnimeListEdgeResponse>

    suspend fun updateUserAnimeListStatus(
        animeId: Int,
        status: String? = null,
        isRewatching: Boolean? = null,
        score: Int? = null,
        numEpisodesWatched: Int? = null,
        priority: Int? = null,
        numTimesRewatched: Int? = null,
        rewatchValue: Int? = null,
        tags: List<String>? = null,
        comments: String? = null,
    ): UserAnimeListStatusResponse

    suspend fun deleteUserAnimeListItem(
        animeId: Int,
    )
}
