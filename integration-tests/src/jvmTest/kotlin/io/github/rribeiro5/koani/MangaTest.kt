package io.github.rribeiro5.koani

import io.github.rribeiro5.koani.manga.MangaField
import io.github.rribeiro5.koani.manga.MangaRankingType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MangaTest : BaseIntegrationTest() {

    @Test
    fun `get manga list with defaults`() = runIntegrationTest { client ->
        val response = performRequest {
            client.manga.getMangaList(query = "Berserk")
        }
        assertNotNull(response)
        assertTrue(response.data.isNotEmpty())
    }

    @Test
    fun `get manga list with query and full list of fields`() = runIntegrationTest { client ->
        val response = performRequest {
            client.manga.getMangaList(
                query = "Monster",
                fields = MangaField.entries
            )
        }
        assertNotNull(response)
        assertTrue(response.data.isNotEmpty())
        val firstManga = response.data.first()
        assertNotNull(firstManga.synopsis)
        assertNotNull(firstManga.authors)
    }

    @Test
    fun `get manga list with limit 3 and request next page`() = runIntegrationTest { client ->
        val limit = 3
        val firstPage = performRequest {
            client.manga.getMangaList(query = "Kingdom", limit = limit)
        }
        assertEquals(limit, firstPage.data.size)
        assertNotNull(firstPage.paging.nextOffset)

        val secondPage = performRequest {
            client.manga.getMangaList(
                query = "Kingdom",
                limit = limit,
                offset = firstPage.paging.nextOffset?.toInt()
            )
        }
        assertEquals(limit, secondPage.data.size)
        assertTrue(firstPage.data.first().id != secondPage.data.first().id)
    }

    @Test
    fun `get manga list and then request the details of the first returned manga`() = runIntegrationTest { client ->
        val list = performRequest {
            client.manga.getMangaList(query = "20th Century Boys", limit = 1)
        }
        val mangaId = list.data.first().id

        val detailsWithoutFields = performRequest {
            client.manga.getMangaDetails(mangaId)
        }
        assertNotNull(detailsWithoutFields)
        assertEquals(mangaId, detailsWithoutFields.id)

        val detailsWithFields = performRequest {
            client.manga.getMangaDetails(mangaId, fields = MangaField.entries)
        }
        assertNotNull(detailsWithFields)
        assertEquals(mangaId, detailsWithFields.id)
        assertNotNull(detailsWithFields.synopsis)
    }

    @Test
    fun `get manga ranking for one of the ranking types`() = runIntegrationTest { client ->
        val response = performRequest {
            client.manga.getMangaRanking(MangaRankingType.All)
        }
        assertNotNull(response)
        assertTrue(response.data.isNotEmpty())
    }
}
