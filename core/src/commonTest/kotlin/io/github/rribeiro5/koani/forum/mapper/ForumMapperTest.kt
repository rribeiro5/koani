package io.github.rribeiro5.koani.forum.mapper

import io.github.rribeiro5.koani.forum.dto.ForumBoardItemResponse
import io.github.rribeiro5.koani.forum.dto.ForumCategoryResponse
import io.github.rribeiro5.koani.forum.dto.ForumPollOptionResponse
import io.github.rribeiro5.koani.forum.dto.ForumPollResponse
import io.github.rribeiro5.koani.forum.dto.ForumPostResponse
import io.github.rribeiro5.koani.forum.dto.ForumSubBoardResponse
import io.github.rribeiro5.koani.forum.dto.ForumTopicDetailDataResponse
import io.github.rribeiro5.koani.forum.dto.ForumTopicDetailResponse
import io.github.rribeiro5.koani.forum.dto.ForumTopicResponse
import io.github.rribeiro5.koani.forum.dto.ForumUserResponse
import io.github.rribeiro5.koani.core.dto.PagingDataResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ForumMapperTest {

    @Test
    fun `ForumCategoryResponse toDomain should map all fields`() {
        val response = ForumCategoryResponse(
            title = "Category",
            boards = listOf(
                ForumBoardItemResponse(
                    id = 1,
                    title = "Board",
                    description = "Desc",
                    subboards = listOf(ForumSubBoardResponse(id = 2, title = "Sub"))
                )
            )
        )

        val domain = response.toDomain()

        assertEquals("Category", domain.title)
        assertEquals(1, domain.boards.size)
        assertEquals(1, domain.boards[0].id)
        assertEquals("Board", domain.boards[0].title)
        assertEquals("Desc", domain.boards[0].description)
        assertEquals(1, domain.boards[0].subboards.size)
        assertEquals(2, domain.boards[0].subboards[0].id)
        assertEquals("Sub", domain.boards[0].subboards[0].title)
    }

    @Test
    fun `ForumTopicResponse toDomain should map all fields`() {
        val response = ForumTopicResponse(
            id = 1,
            title = "Topic",
            createdAt = "2023-08-22T00:00:00Z",
            createdBy = ForumUserResponse(id = 1, name = "Admin", forumAvatar = "url"),
            numberOfPosts = 10,
            lastPostCreatedAt = "2023-08-23T00:00:00Z",
            lastPostCreatedBy = ForumUserResponse(id = 2, name = "User"),
            isLocked = true
        )

        val domain = response.toDomain()

        assertEquals(1, domain.id)
        assertEquals("Topic", domain.title)
        assertEquals(10, domain.numberOfPosts)
        assertEquals(true, domain.isLocked)
        assertEquals(1, domain.createdBy.id)
        assertEquals("Admin", domain.createdBy.name)
        assertEquals("url", domain.createdBy.forumAvatar)
        assertEquals(2, domain.lastPostCreatedBy.id)
        assertEquals("User", domain.lastPostCreatedBy.name)
        assertNull(domain.lastPostCreatedBy.forumAvatar)
    }

    @Test
    fun `ForumTopicDetailResponse toDomain should map all fields including poll`() {
        val response = ForumTopicDetailResponse(
            data = ForumTopicDetailDataResponse(
                title = "Topic",
                posts = listOf(
                    ForumPostResponse(
                        id = 1,
                        number = 1,
                        createdAt = "2023-08-22T00:00:00Z",
                        createdBy = ForumUserResponse(id = 1, name = "Admin"),
                        body = "Body",
                        signature = "Sig"
                    )
                ),
                poll = ForumPollResponse(
                    id = 1,
                    question = "Quest?",
                    isClosed = false,
                    options = listOf(ForumPollOptionResponse(id = 1, text = "Yes", votes = 10))
                )
            ),
            paging = PagingDataResponse(next = "next_url")
        )

        val domain = response.toDomain()

        assertEquals("Topic", domain.title)
        assertEquals(1, domain.posts.data.size)
        assertEquals(1, domain.posts.data[0].id)
        assertEquals("Body", domain.posts.data[0].body)
        assertEquals("Sig", domain.posts.data[0].signature)
        assertNotNull(domain.poll)
        assertEquals(1, domain.poll.id)
        assertEquals("Quest?", domain.poll.question)
        assertEquals(false, domain.poll.isClosed)
        assertEquals(1, domain.poll.options.size)
        assertEquals("Yes", domain.poll.options[0].text)
        assertEquals(10, domain.poll.options[0].votes)
        assertEquals("next_url", domain.posts.paging.nextUrl)
    }
}
