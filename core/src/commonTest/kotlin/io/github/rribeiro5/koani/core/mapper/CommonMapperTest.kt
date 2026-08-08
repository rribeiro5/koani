package io.github.rribeiro5.koani.core.mapper

import io.github.rribeiro5.koani.core.Nsfw
import io.github.rribeiro5.koani.core.dto.AlternativeTitlesResponse
import io.github.rribeiro5.koani.core.dto.GenreResponse
import io.github.rribeiro5.koani.core.dto.PictureResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CommonMapperTest {

    @Test
    fun `PictureResponse toDomain should map correctly`() {
        val response = PictureResponse(
            medium = "medium_url",
            large = "large_url"
        )
        val domain = response.toDomain()

        assertEquals(response.medium, domain.medium)
        assertEquals(response.large, domain.large)
    }

    @Test
    fun `AlternativeTitlesResponse toDomain should map correctly`() {
        val response = AlternativeTitlesResponse(
            synonyms = listOf("synonym1", "synonym2"),
            en = "English Title",
            ja = "Japanese Title"
        )
        val domain = response.toDomain()

        assertEquals(response.synonyms, domain.synonyms)
        assertEquals(response.en, domain.en)
        assertEquals(response.ja, domain.ja)
    }

    @Test
    fun `GenreResponse toDomain should map correctly`() {
        val response = GenreResponse(
            id = 1,
            name = "Action"
        )
        val domain = response.toDomain()

        assertEquals(response.id, domain.id)
        assertEquals(response.name, domain.name)
    }

    @Test
    fun `String toNsfw should map correctly`() {
        assertEquals(Nsfw.White, "white".toNsfw())
        assertEquals(Nsfw.Gray, "gray".toNsfw())
        assertEquals(Nsfw.Black, "black".toNsfw())
        assertNull("unknown".toNsfw())
        assertNull("".toNsfw())
    }
}
