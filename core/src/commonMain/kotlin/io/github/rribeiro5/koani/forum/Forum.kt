package io.github.rribeiro5.koani.forum

import io.github.rribeiro5.koani.core.PaginatedList
import kotlin.time.Instant

public data class ForumCategory(
    val title: String,
    val boards: List<ForumBoard>,
)

public data class ForumBoard(
    val id: Int,
    val title: String,
    val description: String,
    val subboards: List<ForumSubBoard>,
)

public data class ForumSubBoard(
    val id: Int,
    val title: String,
)

public data class ForumTopic(
    val id: Int,
    val title: String,
    val createdAt: Instant,
    val createdBy: ForumUser,
    val numberOfPosts: Int,
    val lastPostCreatedAt: Instant,
    val lastPostCreatedBy: ForumUser,
    val isLocked: Boolean,
)

public data class ForumUser(
    val id: Int,
    val name: String,
    val forumAvatar: String? = null,
)

public data class ForumTopicDetail(
    val title: String,
    val posts: PaginatedList<ForumPost>,
    val poll: ForumPoll? = null,
)

public data class ForumPoll(
    val id: Int,
    val question: String,
    val isClosed: Boolean = false,
    val options: List<ForumPollOption>,
)

public data class ForumPollOption(
    val id: Int,
    val text: String,
    val votes: Int,
)

public data class ForumPost(
    val id: Int,
    val number: Int,
    val createdAt: Instant,
    val createdBy: ForumUser,
    val body: String,
    val signature: String,
)
