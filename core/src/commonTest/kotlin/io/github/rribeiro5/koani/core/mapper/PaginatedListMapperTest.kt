package io.github.rribeiro5.koani.core.mapper

import io.github.rribeiro5.koani.core.dto.PaginatedListResponse
import io.github.rribeiro5.koani.core.dto.PagingDataResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PaginatedListMapperTest {

    @Test
    fun `toPaginatedList should map data and paging correctly`() {
        val response = PaginatedListResponse(
            data = listOf("item1", "item2"),
            paging = PagingDataResponse(
                next = "https://api.myanimelist.net/v2/anime?offset=10&limit=5",
                previous = "https://api.myanimelist.net/v2/anime?offset=0&limit=5"
            )
        )

        val result = response.toPaginatedList { it.uppercase() }

        assertEquals(listOf("ITEM1", "ITEM2"), result.data)
        assertEquals("https://api.myanimelist.net/v2/anime?offset=10&limit=5", result.paging.nextUrl)
        assertEquals("https://api.myanimelist.net/v2/anime?offset=0&limit=5", result.paging.previousUrl)
        assertEquals(10L, result.paging.nextOffset)
        assertEquals(0L, result.paging.previousOffset)
        assertEquals(5L, result.paging.limit)
    }

    @Test
    fun `toPaginatedList should handle null paging correctly`() {
        val response = PaginatedListResponse(
            data = listOf("item1"),
            paging = null
        )

        val result = response.toPaginatedList { it }

        assertEquals(listOf("item1"), result.data)
        assertNull(result.paging.nextUrl)
        assertNull(result.paging.previousUrl)
        assertNull(result.paging.nextOffset)
        assertNull(result.paging.previousOffset)
        assertNull(result.paging.limit)
    }

    @Test
    fun `toPagingData should extract offset and limit correctly from next URL`() {
        val pagingResponse = PagingDataResponse(
            next = "https://api.myanimelist.net/v2/anime?offset=20&limit=10",
            previous = null
        )

        val result = pagingResponse.toPagingData()

        assertEquals(20L, result.nextOffset)
        assertNull(result.previousOffset)
        assertEquals(10L, result.limit)
    }

    @Test
    fun `toPagingData should extract offset and limit correctly from previous URL if next is null`() {
        val pagingResponse = PagingDataResponse(
            next = null,
            previous = "https://api.myanimelist.net/v2/anime?offset=5&limit=15"
        )

        val result = pagingResponse.toPagingData()

        assertNull(result.nextOffset)
        assertEquals(5L, result.previousOffset)
        assertEquals(15L, result.limit)
    }

    @Test
    fun `toPagingData should handle URLs without offset or limit`() {
        val pagingResponse = PagingDataResponse(
            next = "https://api.myanimelist.net/v2/anime",
            previous = "https://api.myanimelist.net/v2/anime"
        )

        val result = pagingResponse.toPagingData()

        assertNull(result.nextOffset)
        assertNull(result.previousOffset)
        assertNull(result.limit)
    }

    @Test
    fun `flatMap should map items while preserving paging`() {
        val paging = PagingDataResponse(
            next = "next-url",
            previous = "prev-url"
        )
        val response = PaginatedListResponse(
            data = listOf("a", "b"),
            paging = paging
        )

        val result = response.flatMap { it.uppercase() }

        assertEquals(listOf("A", "B"), result.data)
        assertEquals(paging, result.paging)
    }
}
