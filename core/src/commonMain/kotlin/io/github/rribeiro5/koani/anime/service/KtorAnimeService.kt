package io.github.rribeiro5.koani.anime.service

import io.github.rribeiro5.koani.anime.dto.AnimeListEdgeResponse
import io.github.rribeiro5.koani.anime.dto.AnimeRankingEdgeResponse
import io.github.rribeiro5.koani.anime.dto.AnimeResponse
import io.github.rribeiro5.koani.anime.dto.UserAnimeListEdgeResponse
import io.github.rribeiro5.koani.anime.dto.UserAnimeListStatusResponse
import io.github.rribeiro5.koani.core.dto.PaginatedListResponse
import io.github.rribeiro5.koani.core.mapper.flatMap
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.Parameters

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

    override suspend fun getUserAnimeList(
        userName: String,
        status: String?,
        sort: String?,
        limit: Int?,
        offset: Int?
    ): PaginatedListResponse<UserAnimeListEdgeResponse> = httpClient.get("v2/users/$userName/animelist") {
        parameter("status", status)
        parameter("sort", sort)
        parameter("limit", limit)
        parameter("offset", offset)
    }.body<PaginatedListResponse<UserAnimeListEdgeResponse>>()

    override suspend fun updateUserAnimeListStatus(
        animeId: Int,
        status: String?,
        isRewatching: Boolean?,
        score: Int?,
        numEpisodesWatched: Int?,
        priority: Int?,
        numTimesRewatched: Int?,
        rewatchValue: Int?,
        tags: List<String>?,
        comments: String?,
    ): UserAnimeListStatusResponse = httpClient.patch("v2/anime/$animeId/my_list_status") {
        setBody(FormDataContent(Parameters.build {
            status?.let { append("status", it) }
            isRewatching?.let { append("is_rewatching", it.toString()) }
            score?.let { append("score", it.toString()) }
            numEpisodesWatched?.let { append("num_watched_episodes", it.toString()) }
            priority?.let { append("priority", it.toString()) }
            numTimesRewatched?.let { append("num_times_rewatched", it.toString()) }
            rewatchValue?.let { append("rewatch_value", it.toString()) }
            tags?.let { append("tags", it.joinToString(",")) }
            comments?.let { append("comments", it) }
        }))
    }.body()

    override suspend fun deleteUserAnimeListItem(
        animeId: Int,
    ) {
        httpClient.delete("v2/anime/$animeId/my_list_status")
    }
}
