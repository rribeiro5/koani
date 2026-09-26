package io.github.rribeiro5.koani.forum.service

import io.github.rribeiro5.koani.core.dto.PaginatedListResponse
import io.github.rribeiro5.koani.forum.dto.ForumBoardResponse
import io.github.rribeiro5.koani.forum.dto.ForumTopicDetailResponse
import io.github.rribeiro5.koani.forum.dto.ForumTopicResponse

internal interface ForumService {
    suspend fun getForumBoards(): ForumBoardResponse

    suspend fun getForumTopics(
        boardId: Int? = null,
        subboardId: Int? = null,
        query: String? = null,
        topicUserName: String? = null,
        userName: String? = null,
        sort: String? = null,
        limit: Int? = null,
        offset: Int? = null,
    ): PaginatedListResponse<ForumTopicResponse>

    suspend fun getForumTopicDetail(
        topicId: Int,
        limit: Int? = null,
        offset: Int? = null,
    ): ForumTopicDetailResponse
}
