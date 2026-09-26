package io.github.rribeiro5.koani.forum

import io.github.rribeiro5.koani.core.PaginatedList
import kotlin.time.Instant

/**
 * Represents a category in the MyAnimeList forum.
 *
 * @property title The title of the category.
 * @property boards The list of boards within this category.
 */
public data class ForumCategory(
    val title: String,
    val boards: List<ForumBoard>,
)

/**
 * Represents a board in the MyAnimeList forum.
 *
 * @property id The unique identifier for the board.
 * @property title The title of the board.
 * @property description A description of the board.
 * @property subboards The list of subboards within this board.
 */
public data class ForumBoard(
    val id: Int,
    val title: String,
    val description: String,
    val subboards: List<ForumSubBoard>,
)

/**
 * Represents a subboard in the MyAnimeList forum.
 *
 * @property id The unique identifier for the subboard.
 * @property title The title of the subboard.
 */
public data class ForumSubBoard(
    val id: Int,
    val title: String,
)

/**
 * Represents a forum topic.
 *
 * @property id The unique identifier for the topic.
 * @property title The title of the topic.
 * @property createdAt The time when the topic was created.
 * @property createdBy The user who created the topic.
 * @property numberOfPosts The number of posts in the topic.
 * @property lastPostCreatedAt The time when the last post was created.
 * @property lastPostCreatedBy The user who created the last post.
 * @property isLocked Whether the topic is locked.
 */
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

/**
 * Represents a forum user.
 *
 * @property id The unique identifier for the user.
 * @property name The name of the user.
 * @property forumAvatar The URL of the user's forum avatar.
 */
public data class ForumUser(
    val id: Int,
    val name: String,
    val forumAvatar: String? = null,
)

/**
 * Represents detailed information about a forum topic, including its posts.
 *
 * @property title The title of the topic.
 * @property posts A paginated list of posts in the topic.
 * @property poll The poll associated with the topic, if any.
 */
public data class ForumTopicDetail(
    val title: String,
    val posts: PaginatedList<ForumPost>,
    val poll: ForumPoll? = null,
)

/**
 * Represents a poll in a forum topic.
 *
 * @property id The unique identifier for the poll.
 * @property question The poll question.
 * @property isClosed Whether the poll is closed for voting.
 * @property options The list of poll options.
 */
public data class ForumPoll(
    val id: Int,
    val question: String,
    val isClosed: Boolean = false,
    val options: List<ForumPollOption>,
)

/**
 * Represents an option in a forum poll.
 *
 * @property id The unique identifier for the option.
 * @property text The text of the option.
 * @property votes The number of votes this option has received.
 */
public data class ForumPollOption(
    val id: Int,
    val text: String,
    val votes: Int,
)

/**
 * Represents a post in a forum topic.
 *
 * @property id The unique identifier for the post.
 * @property number The post number in the topic.
 * @property createdAt The time when the post was created.
 * @property createdBy The user who created the post.
 * @property body The content of the post.
 * @property signature The user's forum signature.
 */
public data class ForumPost(
    val id: Int,
    val number: Int,
    val createdAt: Instant,
    val createdBy: ForumUser,
    val body: String,
    val signature: String,
)
