package io.github.rribeiro5.koani.anime.service

import io.github.rribeiro5.koani.anime.dto.AnimeListEdgeResponse
import io.github.rribeiro5.koani.anime.dto.AnimeResponse
import io.github.rribeiro5.koani.core.dto.PaginatedListResponse
import io.github.rribeiro5.koani.core.mapper.flatMap
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class KtorAnimeService(
    private val httpClient: HttpClient,
) : AnimeService {

    override suspend fun getAnimeList(
        query: String?,
        limit: Int?,
        offset: Int?,
        fields: List<String>?,
    ): PaginatedListResponse<AnimeResponse> = httpClient.get("v2/anime") {
        parameter("q", query)
        parameter("limit", limit)
        parameter("offset", offset)
        fields?.let { parameter("fields", it.joinToString(",")) }
    }.body<PaginatedListResponse<AnimeListEdgeResponse>>().flatMap { it.node }

    override suspend fun getAnimeDetails(
        animeId: Int,
        fields: List<String>?,
    ): AnimeResponse = httpClient.get("v2/anime/$animeId") {
        fields?.let { parameter("fields", it.joinToString(",")) }
    }.body<AnimeResponse>()
}
