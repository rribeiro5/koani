package io.github.rribeiro5.koani.anime.service

import io.github.rribeiro5.koani.anime.dto.AnimeListEdgeResponse
import io.github.rribeiro5.koani.anime.dto.AnimeRankingEdgeResponse
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

    override suspend fun getAnimeRanking(
        rankingType: String,
        limit: Int?,
        offset: Int?,
        fields: List<String>?,
    ): PaginatedListResponse<AnimeRankingEdgeResponse> = httpClient.get("v2/anime/ranking") {
        parameter("ranking_type", rankingType)
        parameter("limit", limit)
        parameter("offset", offset)
        fields?.let { parameter("fields", it.joinToString(",")) }
    }.body<PaginatedListResponse<AnimeRankingEdgeResponse>>()

    override suspend fun getSeasonalAnimes(
        year: Int,
        season: String,
        sort: String?,
        limit: Int?,
        offset: Int?,
        fields: List<String>?,
    ): PaginatedListResponse<AnimeResponse> = httpClient.get("v2/anime/season/$year/$season") {
        parameter("sort", sort)
        parameter("limit", limit)
        parameter("offset", offset)
        fields?.let { parameter("fields", it.joinToString(",")) }
    }.body<PaginatedListResponse<AnimeListEdgeResponse>>().flatMap { it.node }

    override suspend fun getSuggestedAnimes(
        limit: Int?,
        offset: Int?,
        fields: List<String>?,
    ): PaginatedListResponse<AnimeResponse> = httpClient.get("v2/anime/suggestions") {
        parameter("limit", limit)
        parameter("offset", offset)
        fields?.let { parameter("fields", it.joinToString(",")) }
    }.body<PaginatedListResponse<AnimeListEdgeResponse>>().flatMap { it.node }
}
