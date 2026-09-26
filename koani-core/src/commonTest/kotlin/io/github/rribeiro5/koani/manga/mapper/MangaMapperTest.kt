package io.github.rribeiro5.koani.manga.mapper

import io.github.rribeiro5.koani.core.Nsfw
import io.github.rribeiro5.koani.core.dto.AlternativeTitlesResponse
import io.github.rribeiro5.koani.core.dto.GenreResponse
import io.github.rribeiro5.koani.core.dto.PictureResponse
import io.github.rribeiro5.koani.core.dto.RankingResponse
import io.github.rribeiro5.koani.manga.UserMangaListStatusType
import io.github.rribeiro5.koani.manga.MangaStatus
import io.github.rribeiro5.koani.manga.MediaType
import io.github.rribeiro5.koani.manga.dto.AuthorNodeResponse
import io.github.rribeiro5.koani.manga.dto.AuthorResponse
import io.github.rribeiro5.koani.manga.dto.MangaRankingEdgeResponse
import io.github.rribeiro5.koani.manga.dto.MangaResponse
import io.github.rribeiro5.koani.manga.dto.SerializationNodeResponse
import io.github.rribeiro5.koani.manga.dto.SerializationResponse
import io.github.rribeiro5.koani.manga.dto.UserMangaListStatusResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MangaMapperTest {

    @Test
    fun `MangaResponse toDomain should map all fields`() {
        val response = MangaResponse(
            id = 1,
            title = "One Piece",
            mainPicture = PictureResponse("medium", "large"),
            alternativeTitles = AlternativeTitlesResponse(listOf("syn"), "en", "ja"),
            startDate = "1997-07-22",
            synopsis = "synopsis",
            mean = 9.22f,
            rank = 1,
            popularity = 1,
            numListUsers = 100,
            numScoringUsers = 50,
            nsfw = "white",
            createdAt = "2023-01-01T00:00:00Z",
            updatedAt = "2023-01-02T00:00:00Z",
            mediaType = "manga",
            status = "currently_publishing",
            genres = listOf(GenreResponse(1, "Action")),
            myListStatus = UserMangaListStatusResponse(
                status = "reading",
                score = 10,
                numVolumesRead = 5,
                numChaptersRead = 100,
                isRereading = false,
                updatedAt = "2023-01-03T00:00:00Z"
            ),
            numVolumes = 100,
            numChapters = 1000,
            authors = listOf(AuthorResponse(AuthorNodeResponse(1, "Eiichiro", "Oda"), "story_and_art")),
            serialization = listOf(SerializationResponse(SerializationNodeResponse(1, "Shounen Jump"), "serialization"))
        )

        val domain = response.toDomain()

        assertEquals(response.id, domain.id)
        assertEquals(response.title, domain.title)
        assertNotNull(domain.mainPicture)
        assertEquals(response.mainPicture?.medium, domain.mainPicture.medium)
        assertEquals(response.alternativeTitles?.en, domain.alternativeTitles?.en)
        assertEquals(Nsfw.White, domain.nsfw)
        assertEquals(MediaType.Manga, domain.mediaType)
        assertEquals(MangaStatus.CurrentlyPublishing, domain.status)
        assertEquals(1, domain.genres?.size)
        assertEquals("Action", domain.genres?.get(0)?.name)
        assertEquals(UserMangaListStatusType.Reading, domain.myListStatus?.status)
        assertEquals(1, domain.authors?.size)
        assertEquals("Eiichiro", domain.authors?.get(0)?.node?.firstName)
        assertEquals(1, domain.serialization?.size)
        assertEquals("Shounen Jump", domain.serialization?.get(0)?.node?.name)
    }

    @Test
    fun `UserMangaListStatusResponse toDomain should map all fields`() {
        val response = UserMangaListStatusResponse(
            status = "reading",
            score = 10,
            numVolumesRead = 5,
            numChaptersRead = 100,
            isRereading = true,
            updatedAt = "2023-01-01T00:00:00Z",
            startDate = "2023-01-02",
            finishDate = "2023-01-03",
            priority = 1,
            numTimesReread = 2,
            rereadValue = 3,
            tags = listOf("tag1", "tag2"),
            comments = "comment"
        )

        val domain = response.toDomain()

        assertEquals(UserMangaListStatusType.Reading, domain.status)
        assertEquals(10, domain.score)
        assertEquals(5, domain.numVolumesRead)
        assertEquals(100, domain.numChaptersRead)
        assertEquals(true, domain.isRereading)
        assertNotNull(domain.updatedAt)
        assertEquals(2023, domain.startDate?.year)
        assertEquals(2023, domain.finishDate?.year)
        assertEquals(1, domain.priority)
        assertEquals(2, domain.numTimesReread)
        assertEquals(3, domain.rereadValue)
        assertEquals(listOf("tag1", "tag2"), domain.tags)
        assertEquals("comment", domain.comments)
    }

    @Test
    fun `UserMangaListStatusType toApiValue should return correct values`() {
        assertEquals("reading", UserMangaListStatusType.Reading.toApiValue())
        assertEquals("completed", UserMangaListStatusType.Completed.toApiValue())
        assertEquals("on_hold", UserMangaListStatusType.OnHold.toApiValue())
        assertEquals("dropped", UserMangaListStatusType.Dropped.toApiValue())
        assertEquals("plan_to_read", UserMangaListStatusType.PlanToRead.toApiValue())
        assertEquals("unknown", UserMangaListStatusType.Unknown.toApiValue())
    }

    @Test
    fun `Enums should map to null or Unknown when value is invalid`() {
        val mangaResponse = MangaResponse(
            id = 1,
            title = "",
            nsfw = "invalid",
            mediaType = "invalid",
            status = "invalid"
        )
        val domain = mangaResponse.toDomain()
        assertNull(domain.nsfw)
        assertNull(domain.mediaType)
        assertNull(domain.status)

        val myListStatusResponse = UserMangaListStatusResponse(
            status = "invalid",
            score = 0,
            numVolumesRead = 0,
            numChaptersRead = 0,
            isRereading = false,
            updatedAt = "2023-01-01T00:00:00Z"
        )
        assertEquals(UserMangaListStatusType.Unknown, myListStatusResponse.toDomain().status)
    }

    @Test
    fun `MangaRankingEdgeResponse toDomain should map fields`() {
        val response = MangaRankingEdgeResponse(
            node = MangaResponse(id = 1, title = "One Piece"),
            ranking = RankingResponse(rank = 1, previousRank = 2)
        )
        val domain = response.toDomain()
        assertEquals(1, domain.manga.id)
        assertEquals(1, domain.rank)
        assertEquals(2, domain.previousRank)
    }
}
