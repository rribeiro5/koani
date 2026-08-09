package io.github.rribeiro5.koani.manga.service

import io.github.rribeiro5.koani.di.KtorRequestMock
import io.github.rribeiro5.koani.http.fakeHttpClient
import io.github.rribeiro5.koani.manga.dto.MangaResponses
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorMangaServiceTest {

    private fun createSubject(requestHandler: KtorRequestMock) = KtorMangaService(
        httpClient = fakeHttpClient(requestHandler = requestHandler)
    )

    @Test
    fun `getMangaList should return paginated manga list`() = runTest {
        val subject = createSubject {
            respond(
                content = MangaResponses.MANGA_LIST,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getMangaList(query = "one piece")

        assertEquals(1, result.data.size)
        assertEquals(1, result.data[0].id)
        assertEquals("One Piece", result.data[0].title)
        assertEquals("https://api.myanimelist.net/v2/manga?offset=1", result.paging?.next)
    }

    @Test
    fun `getMangaDetails should return manga details`() = runTest {
        val subject = createSubject {
            respond(
                content = MangaResponses.MANGA_DETAILS,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getMangaDetails(mangaId = 1)

        assertEquals(1, result.id)
        assertEquals("One Piece", result.title)
    }

    @Test
    fun `getMangaRanking should return ranked manga list`() = runTest {
        val subject = createSubject {
            respond(
                content = MangaResponses.MANGA_RANKING,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getMangaRanking(rankingType = "all")

        assertEquals(1, result.data.size)
        assertEquals(1, result.data[0].node.id)
        assertEquals("One Piece", result.data[0].node.title)
        assertEquals(1, result.data[0].ranking.rank)
    }

    @Test
    fun `getUserMangaList should return user manga list`() = runTest {
        val subject = createSubject {
            respond(
                content = MangaResponses.USER_MANGA_LIST,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getUserMangaList(userName = "@me")

        assertEquals(1, result.data.size)
        assertEquals(1, result.data[0].node.id)
        assertEquals("reading", result.data[0].listStatus.status)
    }

    @Test
    fun `updateUserMangaListStatus should return updated status`() = runTest {
        val subject = createSubject {
            respond(
                content = MangaResponses.UPDATE_USER_MANGA_LIST_STATUS,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.updateUserMangaListStatus(
            mangaId = 1,
            status = "reading",
            score = 10
        )

        assertEquals("reading", result.status)
        assertEquals(10, result.score)
    }

    @Test
    fun `deleteUserMangaListItem should complete successfully`() = runTest {
        val subject = createSubject {
            respond(
                content = "",
                status = HttpStatusCode.OK
            )
        }

        subject.deleteUserMangaListItem(mangaId = 1)
    }
}
