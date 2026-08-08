package io.github.rribeiro5.koani.anime.mapper

import io.github.rribeiro5.koani.anime.AnimeStatus
import io.github.rribeiro5.koani.anime.DayOfWeek
import io.github.rribeiro5.koani.anime.MediaType
import io.github.rribeiro5.koani.anime.MyListStatusType
import io.github.rribeiro5.koani.anime.Nsfw
import io.github.rribeiro5.koani.anime.Rating
import io.github.rribeiro5.koani.anime.Season
import io.github.rribeiro5.koani.anime.Source
import io.github.rribeiro5.koani.anime.dto.AlternativeTitlesResponse
import io.github.rribeiro5.koani.anime.dto.AnimeNodeResponse
import io.github.rribeiro5.koani.anime.dto.AnimeRankingEdgeResponse
import io.github.rribeiro5.koani.anime.dto.AnimeResponse
import io.github.rribeiro5.koani.anime.dto.BroadcastResponse
import io.github.rribeiro5.koani.anime.dto.GenreResponse
import io.github.rribeiro5.koani.anime.dto.MyListStatusResponse
import io.github.rribeiro5.koani.anime.dto.PictureResponse
import io.github.rribeiro5.koani.anime.dto.RankingResponse
import io.github.rribeiro5.koani.anime.dto.RecommendationResponse
import io.github.rribeiro5.koani.anime.dto.RelatedAnimeResponse
import io.github.rribeiro5.koani.anime.dto.RelatedMangaResponse
import io.github.rribeiro5.koani.anime.dto.StartSeasonResponse
import io.github.rribeiro5.koani.anime.dto.StatisticsResponse
import io.github.rribeiro5.koani.anime.dto.StatisticsStatusResponse
import io.github.rribeiro5.koani.anime.dto.StudioResponse
import io.github.rribeiro5.koani.manga.dto.MangaNodeResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AnimeMapperTest {

    @Test
    fun `AnimeResponse toDomain should map all fields`() {
        val response = AnimeResponse(
            id = 1,
            title = "Cowboy Bebop",
            mainPicture = PictureResponse("medium", "large"),
            alternativeTitles = AlternativeTitlesResponse(listOf("syn"), "en", "ja"),
            startDate = "1998-04-03",
            endDate = "1999-04-24",
            synopsis = "synopsis",
            mean = 8.75f,
            rank = 1,
            popularity = 1,
            numListUsers = 100,
            numScoringUsers = 50,
            nsfw = "white",
            createdAt = "2023-01-01T00:00:00Z",
            updatedAt = "2023-01-02T00:00:00Z",
            mediaType = "tv",
            status = "finished_airing",
            genres = listOf(GenreResponse(1, "Sci-Fi")),
            myListStatus = MyListStatusResponse(
                status = "watching",
                score = 10,
                numEpisodesWatched = 5,
                isRewatching = false,
                updatedAt = "2023-01-03T00:00:00Z"
            ),
            numEpisodes = 26,
            startSeason = StartSeasonResponse(1998, "spring"),
            broadcast = BroadcastResponse("friday", "01:00"),
            source = "original",
            averageEpisodeDuration = 1440,
            rating = "r",
            studios = listOf(StudioResponse(1, "Sunrise"))
        )

        val domain = response.toDomain()

        assertEquals(response.id, domain.id)
        assertEquals(response.title, domain.title)
        assertNotNull(domain.mainPicture)
        assertEquals(response.mainPicture?.medium, domain.mainPicture.medium)
        assertEquals(response.alternativeTitles?.en, domain.alternativeTitles?.en)
        assertEquals(Nsfw.White, domain.nsfw)
        assertEquals(MediaType.Tv, domain.mediaType)
        assertEquals(AnimeStatus.FinishedAiring, domain.status)
        assertEquals(1, domain.genres?.size)
        assertEquals("Sci-Fi", domain.genres?.get(0)?.name)
        assertEquals(MyListStatusType.Watching, domain.myListStatus?.status)
        assertEquals(Season.Spring, domain.startSeason?.season)
        assertEquals(DayOfWeek.Friday, domain.broadcast?.dayOfTheWeek)
        assertEquals(Source.Original, domain.source)
        assertEquals(Rating.R, domain.rating)
        assertEquals(1, domain.studios?.size)
        assertEquals("Sunrise", domain.studios?.get(0)?.name)
    }

    @Test
    fun `PictureResponse toDomain should map fields`() {
        val response = PictureResponse("med", "large")
        val domain = response.toDomain()
        assertEquals(response.medium, domain.medium)
        assertEquals(response.large, domain.large)
    }

    @Test
    fun `GenreResponse toDomain should map fields`() {
        val response = GenreResponse(1, "Action")
        val domain = response.toDomain()
        assertEquals(response.id, domain.id)
        assertEquals(response.name, domain.name)
    }

    @Test
    fun `StartSeasonResponse toDomain should map fields`() {
        val response = StartSeasonResponse(2020, "summer")
        val domain = response.toDomain()
        assertEquals(2020, domain.year)
        assertEquals(Season.Summer, domain.season)
    }

    @Test
    fun `BroadcastResponse toDomain should map fields`() {
        val response = BroadcastResponse("monday", "10:00")
        val domain = response.toDomain()
        assertEquals(DayOfWeek.Monday, domain.dayOfTheWeek)
        assertEquals("10:00", domain.startTime)
    }

    @Test
    fun `RelatedAnimeResponse toDomain should map fields`() {
        val response = RelatedAnimeResponse(
            node = AnimeNodeResponse(1, "Title", null),
            relationType = "sequel",
            relationTypeFormatted = "Sequel"
        )
        val domain = response.toDomain()
        assertEquals(1, domain.node.id)
        assertEquals("sequel", domain.relationType)
    }

    @Test
    fun `RelatedMangaResponse toDomain should map fields`() {
        val response = RelatedMangaResponse(
            node = MangaNodeResponse(1, "Title", null),
            relationType = "adaptation",
            relationTypeFormatted = "Adaptation"
        )
        val domain = response.toDomain()
        assertEquals(1, domain.node.id)
        assertEquals("adaptation", domain.relationType)
    }

    @Test
    fun `RecommendationResponse toDomain should map fields`() {
        val response = RecommendationResponse(
            node = AnimeNodeResponse(1, "Title", null),
            numRecommendations = 5
        )
        val domain = response.toDomain()
        assertEquals(1, domain.node.id)
        assertEquals(5, domain.numRecommendations)
    }

    @Test
    fun `StatisticsResponse toDomain should map fields`() {
        val response = StatisticsResponse(
            status = StatisticsStatusResponse(1, 2, 3, 4, 5),
            numListUsers = 15
        )
        val domain = response.toDomain()
        assertEquals(1, domain.status.watching)
        assertEquals(15, domain.numListUsers)
    }

    @Test
    fun `Enums should map to null or Unknown when value is invalid`() {
        val animeResponse = AnimeResponse(
            id = 1,
            title = "",
            nsfw = "invalid",
            mediaType = "invalid",
            status = "invalid",
            rating = "invalid",
            source = "invalid"
        )
        val domain = animeResponse.toDomain()
        assertNull(domain.nsfw)
        assertNull(domain.mediaType)
        assertNull(domain.status)
        assertNull(domain.rating)
        assertNull(domain.source)

        val startSeasonResponse = StartSeasonResponse(2020, "invalid")
        assertEquals(Season.Unknown, startSeasonResponse.toDomain().season)

        val myListStatusResponse = MyListStatusResponse(
            status = "invalid",
            score = 0,
            numEpisodesWatched = 0,
            isRewatching = false,
            updatedAt = "2023-01-01T00:00:00Z"
        )
        assertEquals(MyListStatusType.Unknown, myListStatusResponse.toDomain().status)

        val broadcastResponse = BroadcastResponse("invalid", "00:00")
        assertNull(broadcastResponse.toDomain().dayOfTheWeek)
    }

    @Test
    fun `AnimeRankingEdgeResponse toDomain should map fields`() {
        val response = AnimeRankingEdgeResponse(
            node = AnimeResponse(id = 1, title = "Cowboy Bebop"),
            ranking = RankingResponse(rank = 1, previousRank = 2)
        )
        val domain = response.toDomain()
        assertEquals(1, domain.anime.id)
        assertEquals(1, domain.rank)
        assertEquals(2, domain.previousRank)
    }
}
