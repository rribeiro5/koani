package io.github.rribeiro5.koani.manga.service

import io.github.rribeiro5.koani.core.dto.PaginatedListResponse
import io.github.rribeiro5.koani.core.mapper.flatMap
import io.github.rribeiro5.koani.manga.dto.MangaListEdgeResponse
import io.github.rribeiro5.koani.manga.dto.MangaRankingEdgeResponse
import io.github.rribeiro5.koani.manga.dto.MangaResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class KtorMangaService(
    private val httpClient: HttpClient,
) : MangaService {

    override suspend fun getMangaList(
        query: String?,
        limit: Int?,
        offset: Int?,
        fields: List<String>?,
    ): PaginatedListResponse<MangaResponse> = httpClient.get("v2/manga") {
        parameter("q", query)
        parameter("limit", limit)
        parameter("offset", offset)
        fields?.let { parameter("fields", it.joinToString(",")) }
    }.body<PaginatedListResponse<MangaListEdgeResponse>>().flatMap { it.node }

    override suspend fun getMangaDetails(
        mangaId: Int,
        fields: List<String>?,
    ): MangaResponse = httpClient.get("v2/manga/$mangaId") {
        fields?.let { parameter("fields", it.joinToString(",")) }
    }.body<MangaResponse>()

    override suspend fun getMangaRanking(
        rankingType: String,
        limit: Int?,
        offset: Int?,
        fields: List<String>?,
    ): PaginatedListResponse<MangaRankingEdgeResponse> = httpClient.get("v2/manga/ranking") {
        parameter("ranking_type", rankingType)
        parameter("limit", limit)
        parameter("offset", offset)
        fields?.let { parameter("fields", it.joinToString(",")) }
    }.body<PaginatedListResponse<MangaRankingEdgeResponse>>()
}
