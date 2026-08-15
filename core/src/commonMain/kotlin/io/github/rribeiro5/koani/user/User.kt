package io.github.rribeiro5.koani.user

import kotlinx.datetime.LocalDate
import kotlin.time.Instant

public data class User(
    val id: Int,
    val name: String,
    val picture: String? = null,
    val gender: String? = null,
    val birthday: LocalDate? = null,
    val location: String? = null,
    val joinedAt: Instant? = null,
    val animeStatistics: UserAnimeStatistics? = null,
    val timeZone: String? = null,
    val isSupporter: Boolean? = null,
)

public data class UserAnimeStatistics(
    val numItemsWatching: Int,
    val numItemsCompleted: Int,
    val numItemsOnHold: Int,
    val numItemsDropped: Int,
    val numItemsPlanToWatch: Int,
    val numItems: Int,
    val numDaysWatched: Double,
    val numDaysWatching: Double,
    val numDaysCompleted: Double,
    val numDaysOnHold: Double,
    val numDaysDropped: Double,
    val numDays: Double,
    val numEpisodes: Int,
    val numTimesRewatched: Int,
    val meanScore: Double,
)
