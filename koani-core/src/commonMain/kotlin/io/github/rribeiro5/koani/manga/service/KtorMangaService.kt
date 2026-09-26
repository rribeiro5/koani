package io.github.rribeiro5.koani.manga.service

import io.github.rribeiro5.koani.core.dto.PaginatedListResponse
import io.github.rribeiro5.koani.core.mapper.flatMap
import io.github.rribeiro5.koani.manga.dto.MangaListEdgeResponse
import io.github.rribeiro5.koani.manga.dto.MangaRankingEdgeResponse
import io.github.rribeiro5.koani.manga.dto.MangaResponse
import io.github.rribeiro5.koani.manga.dto.UserMangaListEdgeResponse
import io.github.rribeiro5.koani.manga.dto.UserMangaListStatusResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.Parameters

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

    override suspend fun getUserMangaList(
        userName: String,
        status: String?,
        sort: String?,
        limit: Int?,
        offset: Int?,
    ): PaginatedListResponse<UserMangaListEdgeResponse> =
        httpClient.get("v2/users/$userName/mangalist") {
            parameter("status", status)
            parameter("sort", sort)
            parameter("limit", limit)
            parameter("offset", offset)
        }.body<PaginatedListResponse<UserMangaListEdgeResponse>>()

    override suspend fun updateUserMangaListStatus(
        mangaId: Int,
        status: String?,
        isRereading: Boolean?,
        score: Int?,
        numVolumesRead: Int?,
        numChaptersRead: Int?,
        priority: Int?,
        numTimesReread: Int?,
        rereadValue: Int?,
        tags: List<String>?,
        comments: String?,
    ): UserMangaListStatusResponse = httpClient.patch("v2/manga/$mangaId/my_list_status") {
        setBody(FormDataContent(Parameters.build {
            status?.let { append("status", it) }
            isRereading?.let { append("is_rereading", it.toString()) }
            score?.let { append("score", it.toString()) }
            numVolumesRead?.let { append("num_volumes_read", it.toString()) }
            numChaptersRead?.let { append("num_chapters_read", it.toString()) }
            priority?.let { append("priority", it.toString()) }
            numTimesReread?.let { append("num_times_reread", it.toString()) }
            rereadValue?.let { append("reread_value", it.toString()) }
            tags?.let { append("tags", it.joinToString(",")) }
            comments?.let { append("comments", it) }
        }))
    }.body()

    override suspend fun deleteUserMangaListItem(mangaId: Int) {
        httpClient.delete("v2/manga/$mangaId/my_list_status")
    }
}
