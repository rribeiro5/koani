package io.github.rribeiro5.koani.forum.service

import io.github.rribeiro5.koani.forum.dto.ForumResponses
import io.github.rribeiro5.koani.di.KtorRequestMock
import io.github.rribeiro5.koani.http.fakeHttpClient
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorForumServiceTest {

    private fun createSubject(requestHandler: KtorRequestMock) = KtorForumService(
        httpClient = fakeHttpClient(requestHandler = requestHandler)
    )

    @Test
    fun `getForumBoards should return forum boards`() = runTest {
        val subject = createSubject {
            respond(
                content = ForumResponses.FORUM_BOARDS,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getForumBoards()

        assertEquals(1, result.categories.size)
        assertEquals("MyAnimeList", result.categories[0].title)
        assertEquals(1, result.categories[0].boards.size)
        assertEquals("News", result.categories[0].boards[0].title)
    }

    @Test
    fun `getForumTopics should return forum topics`() = runTest {
        val subject = createSubject {
            respond(
                content = ForumResponses.FORUM_TOPICS,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getForumTopics(query = "MAL")

        assertEquals(1, result.data.size)
        assertEquals(1, result.data[0].id)
        assertEquals("Welcome to MAL", result.data[0].title)
        assertEquals("https://api.myanimelist.net/v2/forum/topics?offset=10", result.paging?.next)
    }

    @Test
    fun `getForumTopicDetail should return forum topic detail`() = runTest {
        val subject = createSubject {
            respond(
                content = ForumResponses.FORUM_TOPIC_DETAIL,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getForumTopicDetail(topicId = 1)

        assertEquals("Welcome to MAL", result.data.title)
        assertEquals(1, result.data.posts.size)
        assertEquals("Welcome to our community!", result.data.posts[0].body)
        assertEquals("Do you like MAL?", result.data.poll?.question)
    }
}
