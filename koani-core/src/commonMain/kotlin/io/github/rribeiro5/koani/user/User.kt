package io.github.rribeiro5.koani.user

import kotlinx.datetime.LocalDate
import kotlin.time.Instant

/**
 * Represents a user profile on MyAnimeList.
 *
 * @property id The unique identifier for the user.
 * @property name The name of the user.
 * @property picture The URL of the user's profile picture.
 * @property gender The gender of the user.
 * @property birthday The birthday of the user.
 * @property location The location of the user.
 * @property joinedAt The time when the user joined MyAnimeList.
 * @property animeStatistics The user's anime statistics.
 * @property timeZone The user's time zone.
 * @property isSupporter Whether the user is a MyAnimeList supporter.
 */
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

/**
 * Represents anime statistics for a user.
 *
 * @property numItemsWatching Number of items currently being watched.
 * @property numItemsCompleted Number of items completed.
 * @property numItemsOnHold Number of items on hold.
 * @property numItemsDropped Number of items dropped.
 * @property numItemsPlanToWatch Number of items planned to watch.
 * @property numItems Total number of items in the list.
 * @property numDaysWatched Total number of days spent watching.
 * @property numDaysWatching Number of days spent watching items currently in "watching" status.
 * @property numDaysCompleted Number of days spent watching items in "completed" status.
 * @property numDaysOnHold Number of days spent watching items in "on hold" status.
 * @property numDaysDropped Number of days spent watching items in "dropped" status.
 * @property numDays Total number of days.
 * @property numEpisodes Total number of episodes watched.
 * @property numTimesRewatched Number of times items have been rewatched.
 * @property meanScore The mean score given by the user.
 */
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
