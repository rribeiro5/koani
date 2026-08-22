package io.github.rribeiro5.koani.forum.dto

import io.github.rribeiro5.koani.core.dto.PagingDataResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ForumBoardResponse(
    @SerialName("categories")
    val categories: List<ForumCategoryResponse>,
)

@Serializable
internal data class ForumCategoryResponse(
    @SerialName("title")
    val title: String,
    @SerialName("boards")
    val boards: List<ForumBoardItemResponse>,
)

@Serializable
internal data class ForumBoardItemResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("subboards")
    val subboards: List<ForumSubBoardResponse>,
)

@Serializable
internal data class ForumSubBoardResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
)

@Serializable
internal data class ForumTopicResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("created_by")
    val createdBy: ForumUserResponse,
    @SerialName("number_of_posts")
    val numberOfPosts: Int,
    @SerialName("last_post_created_at")
    val lastPostCreatedAt: String,
    @SerialName("last_post_created_by")
    val lastPostCreatedBy: ForumUserResponse,
    @SerialName("is_locked")
    val isLocked: Boolean,
)

@Serializable
internal data class ForumUserResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("forum_avator")
    val forumAvatar: String? = null,
)

@Serializable
internal data class ForumTopicDetailResponse(
    @SerialName("data")
    val data: ForumTopicDetailDataResponse,
    @SerialName("paging")
    val paging: PagingDataResponse? = null,
)

@Serializable
internal data class ForumTopicDetailDataResponse(
    @SerialName("title")
    val title: String,
    @SerialName("posts")
    val posts: List<ForumPostResponse>,
    @SerialName("poll")
    val poll: ForumPollResponse? = null,
)

@Serializable
internal data class ForumPollResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("question")
    val question: String,
    @SerialName("close")
    val isClosed: Boolean,
    @SerialName("options")
    val options: List<ForumPollOptionResponse>,
)

@Serializable
internal data class ForumPollOptionResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("text")
    val text: String,
    @SerialName("votes")
    val votes: Int,
)

@Serializable
internal data class ForumPostResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("number")
    val number: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("created_by")
    val createdBy: ForumUserResponse,
    @SerialName("body")
    val body: String,
    @SerialName("signature")
    val signature: String,
)


