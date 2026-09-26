package io.github.rribeiro5.koani.user.mapper

import io.github.rribeiro5.koani.user.dto.UserAnimeStatisticsResponse
import io.github.rribeiro5.koani.user.dto.UserResponse
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserMapperTest {

    @Test
    fun `UserResponse toDomain should map all fields`() {
        val response = UserResponse(
            id = 1,
            name = "testuser",
            picture = "picture_url",
            gender = "male",
            birthday = "1990-01-01",
            location = "Tokyo",
            joinedAt = "2020-01-01T00:00:00Z",
            animeStatistics = UserAnimeStatisticsResponse(
                numItemsWatching = 1,
                numItemsCompleted = 2,
                numItemsOnHold = 3,
                numItemsDropped = 4,
                numItemsPlanToWatch = 5,
                numItems = 15,
                numDaysWatched = 1.5,
                numDaysWatching = 0.5,
                numDaysCompleted = 1.0,
                numDaysOnHold = 0.0,
                numDaysDropped = 0.0,
                numDays = 3.0,
                numEpisodes = 100,
                numTimesRewatched = 10,
                meanScore = 8.5
            ),
            timeZone = "Asia/Tokyo",
            isSupporter = true
        )

        val domain = response.toDomain()

        assertEquals(response.id, domain.id)
        assertEquals(response.name, domain.name)
        assertEquals(response.picture, domain.picture)
        assertEquals(response.gender, domain.gender)
        assertEquals(LocalDate(1990, 1, 1), domain.birthday)
        assertEquals(response.location, domain.location)
        assertNotNull(domain.joinedAt)
        assertNotNull(domain.animeStatistics)
        assertEquals(1, domain.animeStatistics.numItemsWatching)
        assertEquals(8.5, domain.animeStatistics.meanScore)
        assertEquals(response.timeZone, domain.timeZone)
        assertEquals(response.isSupporter, domain.isSupporter)
    }

    @Test
    fun `UserResponse toDomain should map minimal fields`() {
        val response = UserResponse(
            id = 1,
            name = "testuser"
        )

        val domain = response.toDomain()

        assertEquals(1, domain.id)
        assertEquals("testuser", domain.name)
        assertNull(domain.picture)
        assertNull(domain.birthday)
        assertNull(domain.animeStatistics)
    }
}
