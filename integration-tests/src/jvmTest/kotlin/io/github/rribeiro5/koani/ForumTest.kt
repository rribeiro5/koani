package io.github.rribeiro5.koani

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ForumTest : BaseIntegrationTest() {

    @Test
    fun `get forum boards`() = runIntegrationTest { client ->
        val boards = performRequest {
            client.forum.getForumBoards()
        }
        assertNotNull(boards)
        assertTrue(boards.isNotEmpty())
        val firstCategory = boards.first()
        assertTrue(firstCategory.boards.isNotEmpty())
    }

    @Test
    fun `get forum boards, then use ID of the first to get forum topics`() = runIntegrationTest { client ->
        val boards = performRequest {
            client.forum.getForumBoards()
        }
        val firstBoardId = boards.first().boards.first().id

        val topics = performRequest {
            client.forum.getForumTopics(boardId = firstBoardId)
        }
        assertNotNull(topics)
        assertTrue(topics.data.isNotEmpty())
    }

    @Test
    fun `get forum topics for a query`() = runIntegrationTest { client ->
        val topics = performRequest {
            client.forum.getForumTopics(query = "koani")
        }
        assertNotNull(topics)
    }

    @Test
    fun `get forum topics with limit 3 and then get next page`() = runIntegrationTest { client ->
        val limit = 3
        val firstPage = performRequest {
            client.forum.getForumTopics(limit = limit)
        }
        assertEquals(limit, firstPage.data.size)
        assertNotNull(firstPage.paging.nextOffset)

        val secondPage = performRequest {
            client.forum.getForumTopics(
                limit = limit,
                offset = firstPage.paging.nextOffset?.toInt()
            )
        }
        assertEquals(limit, secondPage.data.size)
        assertTrue(firstPage.data.first().id != secondPage.data.first().id)
    }

    @Test
    fun `get forum topics for a query and then get details for that topic`() = runIntegrationTest { client ->
        val topics = performRequest {
            client.forum.getForumTopics(query = "Naruto")
        }
        assertTrue(topics.data.isNotEmpty())
        val topicId = topics.data.first().id

        val details = performRequest {
            client.forum.getForumTopicDetail(topicId = topicId)
        }
        assertNotNull(details)
        assertTrue(details.posts.data.isNotEmpty())
    }

    @Test
    fun `get forum topics for a query, get details with limit 1 and then fetch next page`() = runIntegrationTest { client ->
        val topics = performRequest {
            client.forum.getForumTopics(query = "One Piece")
        }
        assertTrue(topics.data.isNotEmpty())
        val topicId = topics.data.first().id

        val limit = 1
        val firstDetails = performRequest {
            client.forum.getForumTopicDetail(topicId = topicId, limit = limit)
        }
        assertEquals(limit, firstDetails.posts.data.size)
        assertNotNull(firstDetails.posts.paging.nextOffset)

        val secondDetails = performRequest {
            client.forum.getForumTopicDetail(
                topicId = topicId,
                limit = limit,
                offset = firstDetails.posts.paging.nextOffset?.toInt()
            )
        }
        assertEquals(limit, secondDetails.posts.data.size)
        assertTrue(firstDetails.posts.data.first().id != secondDetails.posts.data.first().id)
    }
}
