package io.github.rribeiro5.koani.forum.service

import io.github.rribeiro5.koani.core.dto.PaginatedListResponse
import io.github.rribeiro5.koani.forum.dto.ForumBoardResponse
import io.github.rribeiro5.koani.forum.dto.ForumTopicDetailResponse
import io.github.rribeiro5.koani.forum.dto.ForumTopicResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class KtorForumService(
    private val httpClient: HttpClient,
) : ForumService {

    override suspend fun getForumBoards(): ForumBoardResponse = httpClient.get("v2/forum/boards").body()

    override suspend fun getForumTopics(
        boardId: Int?,
        subboardId: Int?,
        query: String?,
        topicUserName: String?,
        userName: String?,
        sort: String?,
        limit: Int?,
        offset: Int?,
    ): PaginatedListResponse<ForumTopicResponse> = httpClient.get("v2/forum/topics") {
        parameter("board_id", boardId)
        parameter("subboard_id", subboardId)
        parameter("q", query)
        parameter("topic_user_name", topicUserName)
        parameter("user_name", userName)
        parameter("limit", limit)
        parameter("offset", offset)
        parameter("sort", sort)
    }.body()

    override suspend fun getForumTopicDetail(
        topicId: Int,
        limit: Int?,
        offset: Int?,
    ): ForumTopicDetailResponse = httpClient.get("v2/forum/topic/$topicId") {
        parameter("limit", limit)
        parameter("offset", offset)
    }.body()
}
