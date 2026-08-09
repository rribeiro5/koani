package io.github.rribeiro5.koani

import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Severity
import io.github.rribeiro5.koani.anime.AnimeField
import io.github.rribeiro5.koani.anime.AnimeRankingType
import io.github.rribeiro5.koani.anime.Season
import io.github.rribeiro5.koani.anime.UserAnimeListStatusType
import io.github.rribeiro5.koani.anime.dto.AnimeResponses
import io.github.rribeiro5.koani.auth.MemoryTokenManager
import io.github.rribeiro5.koani.auth.dto.TokenResponses
import io.github.rribeiro5.koani.di.KoaniContainer
import io.github.rribeiro5.koani.di.assertContains
import io.github.rribeiro5.koani.di.fakeContainer
import io.github.rribeiro5.koani.di.fakeLogWriter
import io.github.rribeiro5.koani.error.BadRequestException
import io.github.rribeiro5.koani.error.NotFoundException
import io.github.rribeiro5.koani.error.UnauthorizedException
import io.github.rribeiro5.koani.error.dto.ErrorResponses
import io.github.rribeiro5.koani.manga.MangaField
import io.github.rribeiro5.koani.manga.MangaRankingType
import io.github.rribeiro5.koani.manga.dto.MangaResponses
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalKermitApi::class)
class KoaniClientTest {

    private fun createSubject(container: KoaniContainer): KoaniClient {
        return KoaniClient(container)
    }

    @Test
    fun `init should log successfully initialized`() {
        val logWriter = fakeLogWriter()
        val container = fakeContainer(
            logLevel = LogLevel.DEBUG,
            logWriter = logWriter,
        )

        createSubject(container)

        logWriter.assertContains {
            message == "KoaniClient successfully initialized"
        }
    }

    @Test
    fun `init should throw exception and log when clientId is blank`() {
        val logWriter = fakeLogWriter()
        val container = fakeContainer(
            clientId = " ",
            logWriter = logWriter,
            logLevel = LogLevel.ERROR
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            createSubject(container)
        }

        assertEquals("Client ID cannot be empty", exception.message)
        logWriter.assertContains {
            message == "Client ID cannot be empty" && severity == Severity.Error
        }
    }

    // region Auth tests
    @Test
    fun `authenticate should store tokens and return session on success`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = TokenResponses.SUCCESS,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val session = subject.auth.authenticate("code", "verifier")

        assertEquals("access-token-123", session.accessToken)
        assertEquals("refresh-token-456", session.refreshToken)
        assertEquals("access-token-123", subject.auth.tokenManager.accessToken())
        assertEquals("refresh-token-456", subject.auth.tokenManager.refreshToken())
    }

    @Test
    fun `authenticate should throw BadRequestException on 400 error`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = ErrorResponses.INVALID_GRANT,
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val exception = assertFailsWith<BadRequestException> {
            subject.auth.authenticate("invalid-code", "verifier")
        }

        assertEquals("invalid_grant", exception.error)
        assertEquals(
            "The provided authorization grant is invalid.",
            exception.apiMessage
        )
    }

    @Test
    fun `refreshTokens should store tokens and return session on success`() = runTest {
        val tokenManager = MemoryTokenManager().apply {
            storeTokens("old-access", "old-refresh")
        }
        val container = fakeContainer(
            tokenManager = tokenManager,
            requestHandler = {
                respond(
                    content = TokenResponses.SUCCESS,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val session = subject.auth.refreshTokens()

        assertEquals("access-token-123", session.accessToken)
        assertEquals("refresh-token-456", session.refreshToken)
        assertEquals("access-token-123", subject.auth.tokenManager.accessToken())
        assertEquals("refresh-token-456", subject.auth.tokenManager.refreshToken())
    }

    @Test
    fun `refreshTokens should throw UnauthorizedException on 401 error`() = runTest {
        val tokenManager = MemoryTokenManager().apply {
            storeTokens("old-access", "old-refresh")
        }
        val container = fakeContainer(
            tokenManager = tokenManager,
            requestHandler = {
                respond(
                    content = ErrorResponses.INVALID_GRANT,
                    status = HttpStatusCode.Unauthorized,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val exception = assertFailsWith<UnauthorizedException> {
            subject.auth.refreshTokens()
        }

        assertEquals("invalid_grant", exception.error)
    }

    @Test
    fun `refreshTokens should throw IllegalStateException when no token available`() = runTest {
        val container = fakeContainer()
        val subject = createSubject(container)

        val exception = assertFailsWith<IllegalStateException> {
            subject.auth.refreshTokens(refreshToken = null)
        }

        assertEquals("No refresh token available", exception.message)
    }

    @Test
    fun `logout should clear tokens`() {
        val tokenManager = MemoryTokenManager().apply {
            storeTokens("access", "refresh")
        }
        val container = fakeContainer(tokenManager = tokenManager)
        val subject = createSubject(container)

        subject.auth.logout()

        assertEquals(null, subject.auth.tokenManager.accessToken())
        assertEquals(null, subject.auth.tokenManager.refreshToken())
    }
    // endregion

    // region Anime tests
    @Test
    fun `getAnimeList should return mapped anime list`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = AnimeResponses.ANIME_LIST,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val result = subject.anime.getAnimeList("cowboy")

        assertEquals(1, result.data.size)
        assertEquals("Cowboy Bebop", result.data[0].title)
    }

    @Test
    fun `getAnimeList should pass fields to service`() = runTest {
        var capturedFields: String? = null
        val container = fakeContainer(
            requestHandler = {
                capturedFields = it.url.parameters["fields"]
                respond(
                    content = AnimeResponses.ANIME_LIST,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        subject.anime.getAnimeList(
            query = "cowboy",
            fields = listOf(AnimeField.ID, AnimeField.TITLE, AnimeField.SYNOPSIS)
        )

        assertEquals("id,title,synopsis", capturedFields)
    }

    @Test
    fun `getAnimeDetails should return mapped anime details`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = AnimeResponses.ANIME_DETAILS,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val result = subject.anime.getAnimeDetails(1)

        assertEquals(1, result.id)
        assertEquals("Cowboy Bebop", result.title)
    }

    @Test
    fun `getAnimeDetails should pass fields to service`() = runTest {
        var capturedFields: String? = null
        val container = fakeContainer(
            requestHandler = {
                capturedFields = it.url.parameters["fields"]
                respond(
                    content = AnimeResponses.ANIME_DETAILS,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        subject.anime.getAnimeDetails(
            animeId = 1,
            fields = listOf(AnimeField.ID, AnimeField.MEAN, AnimeField.RANK)
        )

        assertEquals("id,mean,rank", capturedFields)
    }

    @Test
    fun `getAnimeRanking should return ranked anime list`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = AnimeResponses.ANIME_RANKING,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val result = subject.anime.getAnimeRanking(AnimeRankingType.All)

        assertEquals(1, result.data.size)
        assertEquals("Cowboy Bebop", result.data[0].anime.title)
        assertEquals(1, result.data[0].rank)
    }

    @Test
    fun `getSeasonalAnimes should return seasonal anime list`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = AnimeResponses.SEASONAL_ANIME,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val result = subject.anime.getSeasonalAnimes(2024, Season.Spring)

        assertEquals(1, result.data.size)
        assertEquals("Cowboy Bebop", result.data[0].title)
    }

    @Test
    fun `getSuggestedAnimes should return suggested anime list`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = AnimeResponses.SUGGESTED_ANIME,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val result = subject.anime.getSuggestedAnimes()

        assertEquals(1, result.data.size)
        assertEquals("Cowboy Bebop", result.data[0].title)
    }

    @Test
    fun `getUserAnimeList should return mapped user anime list`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = AnimeResponses.USER_ANIME_LIST,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val result = subject.anime.getUserAnimeList()

        assertEquals(1, result.data.size)
        assertEquals("Cowboy Bebop", result.data[0].anime.title)
        assertEquals(UserAnimeListStatusType.Watching, result.data[0].listStatus.status)
    }

    @Test
    fun `updateUserAnimeListStatus should return mapped updated status`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = AnimeResponses.UPDATE_USER_ANIME_LIST_STATUS,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val result = subject.anime.updateUserAnimeListStatus(
            animeId = 1,
            status = UserAnimeListStatusType.Watching,
            score = 10
        )

        assertEquals(UserAnimeListStatusType.Watching, result.status)
        assertEquals(10, result.score)
    }

    @Test
    fun `deleteUserAnimeListItem should complete successfully`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = "",
                    status = HttpStatusCode.OK
                )
            }
        )
        val subject = createSubject(container)

        subject.anime.deleteUserAnimeListItem(animeId = 1)
    }

    @Test
    fun `deleteUserAnimeListItem should throw NotFoundException on 404 error`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = ErrorResponses.NOT_FOUND,
                    status = HttpStatusCode.NotFound,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val exception = assertFailsWith<NotFoundException> {
            subject.anime.deleteUserAnimeListItem(animeId = 1)
        }

        assertEquals("not_found", exception.error)
    }
    // endregion

    // region Manga tests
    @Test
    fun `getMangaList should return mapped manga list`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = MangaResponses.MANGA_LIST,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val result = subject.manga.getMangaList("one piece")

        assertEquals(1, result.data.size)
        assertEquals("One Piece", result.data[0].title)
    }

    @Test
    fun `getMangaList should pass fields to service`() = runTest {
        var capturedFields: String? = null
        val container = fakeContainer(
            requestHandler = {
                capturedFields = it.url.parameters["fields"]
                respond(
                    content = MangaResponses.MANGA_LIST,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        subject.manga.getMangaList(
            query = "one piece",
            fields = listOf(MangaField.ID, MangaField.TITLE, MangaField.SYNOPSIS)
        )

        assertEquals("id,title,synopsis", capturedFields)
    }

    @Test
    fun `getMangaDetails should return mapped manga details`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = MangaResponses.MANGA_DETAILS,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val result = subject.manga.getMangaDetails(1)

        assertEquals(1, result.id)
        assertEquals("One Piece", result.title)
    }

    @Test
    fun `getMangaDetails should pass fields to service`() = runTest {
        var capturedFields: String? = null
        val container = fakeContainer(
            requestHandler = {
                capturedFields = it.url.parameters["fields"]
                respond(
                    content = MangaResponses.MANGA_DETAILS,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        subject.manga.getMangaDetails(
            mangaId = 1,
            fields = listOf(MangaField.ID, MangaField.MEAN, MangaField.RANK)
        )

        assertEquals("id,mean,rank", capturedFields)
    }

    @Test
    fun `getMangaRanking should return ranked manga list`() = runTest {
        val container = fakeContainer(
            requestHandler = {
                respond(
                    content = MangaResponses.MANGA_RANKING,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
        )
        val subject = createSubject(container)

        val result = subject.manga.getMangaRanking(MangaRankingType.All)

        assertEquals(1, result.data.size)
        assertEquals("One Piece", result.data[0].manga.title)
        assertEquals(1, result.data[0].rank)
    }
    // endregion
}
