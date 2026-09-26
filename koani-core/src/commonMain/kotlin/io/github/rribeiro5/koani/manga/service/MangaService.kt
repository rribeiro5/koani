package io.github.rribeiro5.koani.manga.service

import io.github.rribeiro5.koani.core.dto.PaginatedListResponse
import io.github.rribeiro5.koani.manga.dto.MangaRankingEdgeResponse
import io.github.rribeiro5.koani.manga.dto.MangaResponse
import io.github.rribeiro5.koani.manga.dto.UserMangaListEdgeResponse
import io.github.rribeiro5.koani.manga.dto.UserMangaListStatusResponse

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

    suspend fun getUserMangaList(
        userName: String,
        status: String? = null,
        sort: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): PaginatedListResponse<UserMangaListEdgeResponse>

    suspend fun updateUserMangaListStatus(
        mangaId: Int,
        status: String? = null,
        isRereading: Boolean? = null,
        score: Int? = null,
        numVolumesRead: Int? = null,
        numChaptersRead: Int? = null,
        priority: Int? = null,
        numTimesReread: Int? = null,
        rereadValue: Int? = null,
        tags: List<String>? = null,
        comments: String? = null,
    ): UserMangaListStatusResponse

    suspend fun deleteUserMangaListItem(mangaId: Int)
}
