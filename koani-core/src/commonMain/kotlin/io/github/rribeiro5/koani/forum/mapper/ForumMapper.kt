package io.github.rribeiro5.koani.forum.mapper

import io.github.rribeiro5.koani.core.PaginatedList
import io.github.rribeiro5.koani.core.mapper.toDateTime
import io.github.rribeiro5.koani.core.mapper.toPagingData
import io.github.rribeiro5.koani.forum.ForumBoard
import io.github.rribeiro5.koani.forum.ForumCategory
import io.github.rribeiro5.koani.forum.ForumPoll
import io.github.rribeiro5.koani.forum.ForumPollOption
import io.github.rribeiro5.koani.forum.ForumPost
import io.github.rribeiro5.koani.forum.ForumSubBoard
import io.github.rribeiro5.koani.forum.ForumTopic
import io.github.rribeiro5.koani.forum.ForumTopicDetail
import io.github.rribeiro5.koani.forum.ForumUser
import io.github.rribeiro5.koani.forum.dto.ForumBoardItemResponse
import io.github.rribeiro5.koani.forum.dto.ForumCategoryResponse
import io.github.rribeiro5.koani.forum.dto.ForumPollOptionResponse
import io.github.rribeiro5.koani.forum.dto.ForumPollResponse
import io.github.rribeiro5.koani.forum.dto.ForumPostResponse
import io.github.rribeiro5.koani.forum.dto.ForumSubBoardResponse
import io.github.rribeiro5.koani.forum.dto.ForumTopicDetailResponse
import io.github.rribeiro5.koani.forum.dto.ForumTopicResponse
import io.github.rribeiro5.koani.forum.dto.ForumUserResponse
import kotlin.time.Instant

internal fun ForumCategoryResponse.toDomain(): ForumCategory = ForumCategory(
    title = title,
    boards = boards.map { it.toDomain() }
)

private fun ForumBoardItemResponse.toDomain(): ForumBoard = ForumBoard(
    id = id,
    title = title,
    description = description,
    subboards = subboards.map { it.toDomain() }
)

private fun ForumSubBoardResponse.toDomain(): ForumSubBoard = ForumSubBoard(
    id = id,
    title = title
)

internal fun ForumTopicResponse.toDomain(): ForumTopic = ForumTopic(
    id = id,
    title = title,
    createdAt = createdAt.toDateTime() ?: Instant.fromEpochMilliseconds(0),
    createdBy = createdBy.toDomain(),
    numberOfPosts = numberOfPosts,
    lastPostCreatedAt = lastPostCreatedAt.toDateTime() ?: Instant.fromEpochMilliseconds(0),
    lastPostCreatedBy = lastPostCreatedBy.toDomain(),
    isLocked = isLocked
)

private fun ForumUserResponse.toDomain(): ForumUser = ForumUser(
    id = id,
    name = name,
    forumAvatar = forumAvatar
)

internal fun ForumTopicDetailResponse.toDomain(): ForumTopicDetail = ForumTopicDetail(
    title = data.title,
    posts = PaginatedList(
        data = data.posts.map { it.toDomain() },
        paging = paging.toPagingData()
    ),
    poll = data.poll?.toDomain()
)

private fun ForumPollResponse.toDomain(): ForumPoll = ForumPoll(
    id = id,
    question = question,
    isClosed = isClosed ?: false,
    options = options.map { it.toDomain() }
)

private fun ForumPollOptionResponse.toDomain(): ForumPollOption = ForumPollOption(
    id = id,
    text = text,
    votes = votes
)

private fun ForumPostResponse.toDomain(): ForumPost = ForumPost(
    id = id,
    number = number,
    createdAt = createdAt.toDateTime() ?: Instant.fromEpochMilliseconds(0),
    createdBy = createdBy.toDomain(),
    body = body,
    signature = signature
)
