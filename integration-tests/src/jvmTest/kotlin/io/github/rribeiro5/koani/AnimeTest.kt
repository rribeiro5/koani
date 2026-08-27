package io.github.rribeiro5.koani

import io.github.rribeiro5.koani.anime.AnimeField
import io.github.rribeiro5.koani.anime.AnimeRankingType
import io.github.rribeiro5.koani.anime.Season
import io.github.rribeiro5.koani.anime.SeasonalAnimeSort
import io.github.rribeiro5.koani.anime.UserAnimeListSortOption
import io.github.rribeiro5.koani.anime.UserAnimeListStatusType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AnimeTest : BaseIntegrationTest() {

    @Test
    fun `get anime list with defaults`() = runIntegrationTest { client ->
        val response = performRequest {
            client.anime.getAnimeList(query = "Naruto")
        }
        assertNotNull(response)
        assertTrue(response.data.isNotEmpty())
    }

    @Test
    fun `get anime list with query and full list of fields`() = runIntegrationTest { client ->
        val response = performRequest {
            client.anime.getAnimeList(
                query = "One Piece",
                fields = AnimeField.entries
            )
        }
        assertNotNull(response)
        assertTrue(response.data.isNotEmpty())
        val firstAnime = response.data.first()
        assertNotNull(firstAnime.synopsis)
        assertNotNull(firstAnime.genres)
    }

    @Test
    fun `get anime list with limit 3 and request next page`() = runIntegrationTest { client ->
        val limit = 3
        val firstPage = performRequest {
            client.anime.getAnimeList(query = "Bleach", limit = limit)
        }
        assertEquals(limit, firstPage.data.size)
        assertNotNull(firstPage.paging.nextOffset)

        val secondPage = performRequest {
            client.anime.getAnimeList(
                query = "Bleach",
                limit = limit,
                offset = firstPage.paging.nextOffset?.toInt()
            )
        }
        assertEquals(limit, secondPage.data.size)
        assertTrue(firstPage.data.first().id != secondPage.data.first().id)
    }

    @Test
    fun `get anime list and then request the details of the first returned anime`() = runIntegrationTest { client ->
        val list = performRequest {
            client.anime.getAnimeList(query = "Fullmetal Alchemist", limit = 1)
        }
        val animeId = list.data.first().id

        val detailsWithoutFields = performRequest {
            client.anime.getAnimeDetails(animeId)
        }
        assertNotNull(detailsWithoutFields)
        assertEquals(animeId, detailsWithoutFields.id)

        val detailsWithFields = performRequest {
            client.anime.getAnimeDetails(animeId, fields = AnimeField.entries)
        }
        assertNotNull(detailsWithFields)
        assertEquals(animeId, detailsWithFields.id)
        assertNotNull(detailsWithFields.synopsis)
    }

    @Test
    fun `get anime ranking for one of the ranking types`() = runIntegrationTest { client ->
        val response = performRequest {
            client.anime.getAnimeRanking(AnimeRankingType.All)
        }
        assertNotNull(response)
        assertTrue(response.data.isNotEmpty())
    }

    @Test
    fun `get seasonal animes for Winter 2021 without sort option`() = runIntegrationTest { client ->
        val response = performRequest {
            client.anime.getSeasonalAnimes(2021, Season.Winter)
        }
        assertNotNull(response)
        assertTrue(response.data.isNotEmpty())
    }

    @Test
    fun `get seasonal animes for Winter 2021 with one sort option`() = runIntegrationTest { client ->
        val response = performRequest {
            client.anime.getSeasonalAnimes(2021, Season.Winter, sort = SeasonalAnimeSort.AnimeScore)
        }
        assertNotNull(response)
        assertTrue(response.data.isNotEmpty())
    }

    @Test
    fun `get user anime list`() = runIntegrationTest { client ->
        val response = performRequest {
            client.anime.getUserAnimeList(userName = "rafa_ribeiro1")
        }
        assertNotNull(response)
        assertTrue(response.data.isNotEmpty())
    }

    @Test
    fun `get user anime list for a status type`() = runIntegrationTest { client ->
        val response = performRequest {
            client.anime.getUserAnimeList(userName = "rafa_ribeiro1", status = UserAnimeListStatusType.Completed)
        }
        assertNotNull(response)
        assertTrue(response.data.isNotEmpty())
        // When unauthenticated (Client ID only), listStatus is omitted by the API
    }

    @Test
    fun `get user anime list with a sort option`() = runIntegrationTest { client ->
        val response = performRequest {
            client.anime.getUserAnimeList(userName = "rafa_ribeiro1", sortOption = UserAnimeListSortOption.Title)
        }
        assertNotNull(response)
        assertTrue(response.data.isNotEmpty())
    }
}
