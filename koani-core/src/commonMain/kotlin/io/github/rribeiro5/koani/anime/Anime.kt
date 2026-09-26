package io.github.rribeiro5.koani.anime

import io.github.rribeiro5.koani.core.AlternativeTitles
import io.github.rribeiro5.koani.core.Genre
import io.github.rribeiro5.koani.core.Nsfw
import io.github.rribeiro5.koani.core.Picture
import io.github.rribeiro5.koani.manga.MangaNode
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

/**
 * Represents an anime in the MyAnimeList database.
 *
 * @property id The unique identifier for the anime.
 * @property title The main title of the anime.
 * @property mainPicture The main picture of the anime.
 * @property alternativeTitles Alternative titles (English, Japanese, synonyms).
 * @property startDate The date when the anime started airing.
 * @property endDate The date when the anime finished airing.
 * @property synopsis A brief description of the anime.
 * @property mean The mean score of the anime.
 * @property rank The current rank of the anime.
 * @property popularity The popularity rank of the anime.
 * @property numListUsers The number of users who have the anime in their list.
 * @property numScoringUsers The number of users who have scored the anime.
 * @property nsfw The NSFW rating of the anime.
 * @property createdAt The time when the anime record was created.
 * @property updatedAt The time when the anime record was last updated.
 * @property mediaType The media type of the anime (e.g., TV, OVA).
 * @property status The airing status of the anime.
 * @property genres The genres associated with the anime.
 * @property myListStatus The status of the anime in the authenticated user's list.
 * @property numEpisodes The total number of episodes.
 * @property startSeason The season and year when the anime started airing.
 * @property broadcast The broadcast schedule of the anime.
 * @property source The source material of the anime.
 * @property averageEpisodeDuration The average duration of an episode in seconds.
 * @property rating The age rating of the anime.
 * @property pictures Additional pictures of the anime.
 * @property background Background information about the anime.
 * @property relatedAnime A list of related anime.
 * @property relatedManga A list of related manga.
 * @property recommendations A list of recommended anime based on this one.
 * @property studios The studios that produced the anime.
 * @property statistics Statistical data about the anime.
 */
public data class Anime(
    val id: Int,
    val title: String,
    val mainPicture: Picture? = null,
    val alternativeTitles: AlternativeTitles? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val synopsis: String? = null,
    val mean: Float? = null,
    val rank: Int? = null,
    val popularity: Int? = null,
    val numListUsers: Int? = null,
    val numScoringUsers: Int? = null,
    val nsfw: Nsfw? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val mediaType: MediaType? = null,
    val status: AnimeStatus? = null,
    val genres: List<Genre>? = null,
    val myListStatus: UserAnimeListStatus? = null,
    val numEpisodes: Int? = null,
    val startSeason: StartSeason? = null,
    val broadcast: Broadcast? = null,
    val source: Source? = null,
    val averageEpisodeDuration: Int? = null,
    val rating: Rating? = null,
    val pictures: List<Picture>? = null,
    val background: String? = null,
    val relatedAnime: List<RelatedAnime>? = null,
    val relatedManga: List<RelatedManga>? = null,
    val recommendations: List<Recommendation>? = null,
    val studios: List<Studio>? = null,
    val statistics: Statistics? = null,
)

/**
 * Represents an anime with its ranking information.
 *
 * @property anime The anime details.
 * @property rank The current rank.
 * @property previousRank The previous rank, if available.
 */
public data class RankedAnime(
    val anime: Anime,
    val rank: Int,
    val previousRank: Int? = null,
)

/**
 * Represents an item in a user's anime list.
 *
 * @property anime The anime details.
 * @property listStatus The status of the anime in the user's list.
 */
public data class UserAnimeListItem(
    val anime: Anime,
    val listStatus: UserAnimeListStatus? = null,
)

/**
 * Represents the status of an anime in a user's list.
 *
 * @property status The user's status for this anime (e.g., watching, completed).
 * @property score The user's score for the anime (0-10).
 * @property numEpisodesWatched The number of episodes watched by the user.
 * @property isRewatching Whether the user is currently rewatching the anime.
 * @property updatedAt The time when the list status was last updated.
 * @property startDate The date when the user started watching.
 * @property finishDate The date when the user finished watching.
 * @property priority The priority of the anime in the user's list.
 * @property numTimesRewatched The number of times the user has rewatched the anime.
 * @property rewatchValue The value assigned to rewatching.
 * @property tags Tags added by the user to this anime.
 * @property comments Comments added by the user to this anime.
 */
public data class UserAnimeListStatus(
    val status: UserAnimeListStatusType,
    val score: Int,
    val numEpisodesWatched: Int,
    val isRewatching: Boolean,
    val updatedAt: Instant,
    val startDate: LocalDate? = null,
    val finishDate: LocalDate? = null,
    val priority: Int? = null,
    val numTimesRewatched: Int? = null,
    val rewatchValue: Int? = null,
    val tags: List<String> = emptyList(),
    val comments: String? = null,
)

/**
 * Represents the starting season of an anime.
 *
 * @property year The year of the season.
 * @property season The season (e.g., Winter, Spring).
 */
public data class StartSeason(
    val year: Int,
    val season: Season,
)

/**
 * Represents the broadcast schedule of an anime.
 *
 * @property dayOfTheWeek The day of the week the anime is broadcast.
 * @property startTime The start time of the broadcast (HH:mm).
 */
public data class Broadcast(
    val dayOfTheWeek: DayOfWeek? = null,
    val startTime: String? = null,
)

/**
 * Represents a relationship between this anime and another anime.
 *
 * @property node The related anime node.
 * @property relationType The type of relationship (e.g., sequel, prequel).
 * @property relationTypeFormatted The formatted name of the relationship type.
 */
public data class RelatedAnime(
    val node: AnimeNode,
    val relationType: String,
    val relationTypeFormatted: String,
)

/**
 * Represents a relationship between this anime and a manga.
 *
 * @property node The related manga node.
 * @property relationType The type of relationship (e.g., adaptation).
 * @property relationTypeFormatted The formatted name of the relationship type.
 */
public data class RelatedManga(
    val node: MangaNode,
    val relationType: String,
    val relationTypeFormatted: String,
)

/**
 * Represents a recommendation for this anime.
 *
 * @property node The recommended anime node.
 * @property numRecommendations The number of users who recommended this.
 */
public data class Recommendation(
    val node: AnimeNode,
    val numRecommendations: Int,
)

/**
 * Represents a production studio.
 *
 * @property id The unique identifier for the studio.
 * @property name The name of the studio.
 */
public data class Studio(
    val id: Int,
    val name: String,
)

/**
 * Represents statistics about an anime.
 *
 * @property status The breakdown of users' list statuses for this anime.
 * @property numListUsers Total number of users who have this anime in their list.
 */
public data class Statistics(
    val status: StatisticsStatus,
    val numListUsers: Int,
)

/**
 * Represents the breakdown of list statuses in statistics.
 *
 * @property watching Number of users currently watching.
 * @property completed Number of users who have completed the anime.
 * @property onHold Number of users who have put the anime on hold.
 * @property dropped Number of users who have dropped the anime.
 * @property planToWatch Number of users who plan to watch the anime.
 */
public data class StatisticsStatus(
    val watching: Int,
    val completed: Int,
    val onHold: Int,
    val dropped: Int,
    val planToWatch: Int,
)

/**
 * A simplified anime node used in relationships and recommendations.
 *
 * @property id The unique identifier for the anime.
 * @property title The title of the anime.
 * @property mainPicture The main picture of the anime.
 */
public data class AnimeNode(
    val id: Int,
    val title: String,
    val mainPicture: Picture? = null,
)

/**
 * The media type of an anime.
 */
public enum class MediaType {
    /** Unknown media type. */
    Unknown,
    /** TV series. */
    Tv,
    /** Original Video Animation. */
    Ova,
    /** Movie. */
    Movie,
    /** Special episodes. */
    Special,
    /** Original Net Animation. */
    Ona,
    /** Music video. */
    Music
}

/**
 * The airing status of an anime.
 */
public enum class AnimeStatus {
    /** The anime has finished airing. */
    FinishedAiring,
    /** The anime is currently airing. */
    CurrentlyAiring,
    /** The anime has not yet aired. */
    NotYetAired
}

/**
 * The season when an anime started airing.
 */
public enum class Season(internal val value: String) {
    /** Unknown season. */
    Unknown("unknown"),
    /** Winter season (January, February, March). */
    Winter("winter"),
    /** Spring season (April, May, June). */
    Spring("spring"),
    /** Summer season (July, August, September). */
    Summer("summer"),
    /** Fall season (October, November, December). */
    Fall("fall")
}

/**
 * The age rating of an anime.
 */
public enum class Rating {
    /** G - All Ages. */
    G,
    /** PG - Children. */
    Pg,
    /** PG-13 - Teens 13 or older. */
    Pg13,
    /** R - 17+ (violence & profanity). */
    R,
    /** R+ - Mild Nudity. */
    RPlus,
    /** Rx - Hentai. */
    Rx
}

/**
 * The status of an anime in a user's list.
 */
public enum class UserAnimeListStatusType {
    /** Unknown status. */
    Unknown,
    /** The user is currently watching the anime. */
    Watching,
    /** The user has completed the anime. */
    Completed,
    /** The user has put the anime on hold. */
    OnHold,
    /** The user has dropped the anime. */
    Dropped,
    /** The user plans to watch the anime. */
    PlanToWatch
}

/**
 * The source material of an anime.
 */
public enum class Source {
    /** Original work. */
    Original,
    /** Manga source. */
    Manga,
    /** 4-koma manga source. */
    FourKomaManga,
    /** Web manga source. */
    WebManga,
    /** Digital manga source. */
    DigitalManga,
    /** Novel source. */
    Novel,
    /** Light novel source. */
    LightNovel,
    /** Visual novel source. */
    VisualNovel,
    /** Game source. */
    Game,
    /** Video game source. */
    VideoGame,
    /** Card game source. */
    CardGame,
    /** Book source. */
    Book,
    /** Picture book source. */
    PictureBook,
    /** Radio source. */
    Radio,
    /** Music source. */
    Music,
    /** Other sources. */
    Other
}

/**
 * The day of the week.
 */
public enum class DayOfWeek {
    /** Monday. */
    Monday,
    /** Tuesday. */
    Tuesday,
    /** Wednesday. */
    Wednesday,
    /** Thursday. */
    Thursday,
    /** Friday. */
    Friday,
    /** Saturday. */
    Saturday,
    /** Sunday. */
    Sunday,
    /** Other days. */
    Other
}

/**
 * The type of anime ranking.
 */
public enum class AnimeRankingType(internal val value: String) {
    /**
     * Top Anime Series.
     */
    All("all"),
    /**
     * Top Airing Anime.
     */
    Airing("airing"),
    /**
     * Top Upcoming Anime.
     */
    Upcoming("upcoming"),
    /**
     * Top TV Anime.
     */
    Tv("tv"),
    /**
     * Top OVA Anime.
     */
    Ova("ova"),
    /**
     * Top Movie Anime.
     */
    Movie("movie"),
    /**
     * Top Special Anime.
     */
    Special("special"),
    /**
     * Top Anime by Popularity.
     */
    ByPopularity("bypopularity"),
    /**
     * Top Favorite Anime.
     */
    Favorite("favorite")
}

/**
 * Sorting options for seasonal anime.
 */
public enum class SeasonalAnimeSort(internal val value: String) {
    /** Sort by anime score. */
    AnimeScore("anime_score"),
    /** Sort by number of users who have the anime in their list. */
    AnimeNumListUsers("anime_num_list_users")
}

/**
 * Sorting options for a user's anime list.
 */
public enum class UserAnimeListSortOption(internal val value: String) {
    /** Sort by the score in the user's list. */
    Score("list_score"),
    /** Sort by the last update time in the user's list. */
    UpdatedAt("list_updated_at"),
    /** Sort by anime title. */
    Title("anime_title"),
    /** Sort by anime start date. */
    StartDate("anime_start_date"),
}
