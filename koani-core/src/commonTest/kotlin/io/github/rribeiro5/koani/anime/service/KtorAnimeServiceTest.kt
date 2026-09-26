package io.github.rribeiro5.koani.anime.service

import io.github.rribeiro5.koani.anime.dto.AnimeResponses
import io.github.rribeiro5.koani.di.KtorRequestMock
import io.github.rribeiro5.koani.http.fakeHttpClient
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorAnimeServiceTest {

    private fun createSubject(requestHandler: KtorRequestMock) = KtorAnimeService(
        httpClient = fakeHttpClient(requestHandler = requestHandler)
    )

    @Test
    fun `getAnimeList should return paginated anime list`() = runTest {
        val subject = createSubject {
            respond(
                content = AnimeResponses.ANIME_LIST,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getAnimeList(query = "cowboy")

        assertEquals(1, result.data.size)
        assertEquals(1, result.data[0].id)
        assertEquals("Cowboy Bebop", result.data[0].title)
        assertEquals("https://api.myanimelist.net/v2/anime?offset=1", result.paging?.next)
    }

    @Test
    fun `getAnimeDetails should return anime details`() = runTest {
        val subject = createSubject {
            respond(
                content = AnimeResponses.ANIME_DETAILS,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getAnimeDetails(animeId = 1)

        assertEquals(1, result.id)
        assertEquals("Cowboy Bebop", result.title)
    }

    @Test
    fun `getAnimeRanking should return ranked anime list`() = runTest {
        val subject = createSubject {
            respond(
                content = AnimeResponses.ANIME_RANKING,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getAnimeRanking(rankingType = "all")

        assertEquals(1, result.data.size)
        assertEquals(1, result.data[0].node.id)
        assertEquals("Cowboy Bebop", result.data[0].node.title)
        assertEquals(1, result.data[0].ranking.rank)
    }

    @Test
    fun `getSeasonalAnimes should return seasonal anime list`() = runTest {
        val subject = createSubject {
            respond(
                content = AnimeResponses.SEASONAL_ANIME,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getSeasonalAnimes(year = 2024, season = "spring")

        assertEquals(1, result.data.size)
        assertEquals(1, result.data[0].id)
        assertEquals("Cowboy Bebop", result.data[0].title)
    }

    @Test
    fun `getSuggestedAnimes should return suggested anime list`() = runTest {
        val subject = createSubject {
            respond(
                content = AnimeResponses.SUGGESTED_ANIME,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getSuggestedAnimes()

        assertEquals(1, result.data.size)
        assertEquals(1, result.data[0].id)
        assertEquals("Cowboy Bebop", result.data[0].title)
    }

    @Test
    fun `getUserAnimeList should return user anime list`() = runTest {
        val subject = createSubject {
            respond(
                content = AnimeResponses.USER_ANIME_LIST,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.getUserAnimeList(userName = "@me")

        assertEquals(1, result.data.size)
        assertEquals(1, result.data[0].node.id)
        assertEquals("watching", result.data[0].listStatus!!.status)
    }

    @Test
    fun `updateUserAnimeListStatus should return updated status`() = runTest {
        val subject = createSubject {
            respond(
                content = AnimeResponses.UPDATE_USER_ANIME_LIST_STATUS,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = subject.updateUserAnimeListStatus(
            animeId = 1,
            status = "watching",
            score = 10
        )

        assertEquals("watching", result.status)
        assertEquals(10, result.score)
    }

    @Test
    fun `deleteUserAnimeListItem should complete successfully`() = runTest {
        val subject = createSubject {
            respond(
                content = "",
                status = HttpStatusCode.OK
            )
        }

        subject.deleteUserAnimeListItem(animeId = 1)
    }
}
