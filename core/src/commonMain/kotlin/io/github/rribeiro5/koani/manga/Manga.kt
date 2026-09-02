package io.github.rribeiro5.koani.manga

import io.github.rribeiro5.koani.anime.AnimeNode
import io.github.rribeiro5.koani.core.AlternativeTitles
import io.github.rribeiro5.koani.core.Genre
import io.github.rribeiro5.koani.core.Nsfw
import io.github.rribeiro5.koani.core.Picture
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

/**
 * Represents a manga in the MyAnimeList database.
 *
 * @property id The unique identifier for the manga.
 * @property title The main title of the manga.
 * @property mainPicture The main picture of the manga.
 * @property alternativeTitles Alternative titles (English, Japanese, synonyms).
 * @property startDate The date when the manga started publishing.
 * @property endDate The date when the manga finished publishing.
 * @property synopsis A brief description of the manga.
 * @property mean The mean score of the manga.
 * @property rank The current rank of the manga.
 * @property popularity The popularity rank of the manga.
 * @property numListUsers The number of users who have the manga in their list.
 * @property numScoringUsers The number of users who have scored the manga.
 * @property nsfw The NSFW rating of the manga.
 * @property createdAt The time when the manga record was created.
 * @property updatedAt The time when the manga record was last updated.
 * @property mediaType The media type of the manga (e.g., manga, novel).
 * @property status The publishing status of the manga.
 * @property genres The genres associated with the manga.
 * @property myListStatus The status of the manga in the authenticated user's list.
 * @property numVolumes The total number of volumes.
 * @property numChapters The total number of chapters.
 * @property authors The authors of the manga.
 * @property pictures Additional pictures of the manga.
 * @property background Background information about the manga.
 * @property relatedAnime A list of related anime.
 * @property relatedManga A list of related manga.
 * @property recommendations A list of recommended manga based on this one.
 * @property serialization The serialization information.
 */
public data class Manga(
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
    val status: MangaStatus? = null,
    val genres: List<Genre>? = null,
    val myListStatus: UserMangaListStatus? = null,
    val numVolumes: Int? = null,
    val numChapters: Int? = null,
    val authors: List<Author>? = null,
    val pictures: List<Picture>? = null,
    val background: String? = null,
    val relatedAnime: List<RelatedAnime>? = null,
    val relatedManga: List<RelatedManga>? = null,
    val recommendations: List<Recommendation>? = null,
    val serialization: List<Serialization>? = null,
)

/**
 * Represents a manga with its ranking information.
 *
 * @property manga The manga details.
 * @property rank The current rank.
 * @property previousRank The previous rank, if available.
 */
public data class RankedManga(
    val manga: Manga,
    val rank: Int,
    val previousRank: Int? = null,
)

/**
 * Represents an item in a user's manga list.
 *
 * @property manga The manga details.
 * @property listStatus The status of the manga in the user's list.
 */
public data class UserMangaListItem(
    val manga: Manga,
    val listStatus: UserMangaListStatus? = null,
)

/**
 * Represents the status of a manga in a user's list.
 *
 * @property status The user's status for this manga (e.g., reading, completed).
 * @property score The user's score for the manga (0-10).
 * @property numVolumesRead The number of volumes read by the user.
 * @property numChaptersRead The number of chapters read by the user.
 * @property isRereading Whether the user is currently rereading the manga.
 * @property updatedAt The time when the list status was last updated.
 * @property startDate The date when the user started reading.
 * @property finishDate The date when the user finished reading.
 * @property priority The priority of the manga in the user's list.
 * @property numTimesReread The number of times the user has reread the manga.
 * @property rereadValue The value assigned to rereading.
 * @property tags Tags added by the user to this manga.
 * @property comments Comments added by the user to this manga.
 */
public data class UserMangaListStatus(
    val status: UserMangaListStatusType? = null,
    val score: Int,
    val numVolumesRead: Int,
    val numChaptersRead: Int,
    val isRereading: Boolean,
    val updatedAt: Instant,
    val startDate: LocalDate? = null,
    val finishDate: LocalDate? = null,
    val priority: Int? = null,
    val numTimesReread: Int? = null,
    val rereadValue: Int? = null,
    val tags: List<String> = emptyList(),
    val comments: String? = null,
)

/**
 * The status of a manga in a user's list.
 */
public enum class UserMangaListStatusType {
    /** Unknown status. */
    Unknown,
    /** The user is currently reading the manga. */
    Reading,
    /** The user has completed the manga. */
    Completed,
    /** The user has put the manga on hold. */
    OnHold,
    /** The user has dropped the manga. */
    Dropped,
    /** The user plans to read the manga. */
    PlanToRead
}

/**
 * Represents an author of a manga.
 *
 * @property node The author node details.
 * @property role The role of the author (e.g., Story, Art).
 */
public data class Author(
    val node: AuthorNode,
    val role: String? = null,
)

/**
 * Represents an author node.
 *
 * @property id The unique identifier for the author.
 * @property firstName The first name of the author.
 * @property lastName The last name of the author.
 */
public data class AuthorNode(
    val id: Int,
    val firstName: String? = null,
    val lastName: String? = null,
)

/**
 * Represents a relationship between this manga and an anime.
 *
 * @property node The related anime node.
 * @property relationType The type of relationship.
 * @property relationTypeFormatted The formatted name of the relationship type.
 */
public data class RelatedAnime(
    val node: AnimeNode,
    val relationType: String,
    val relationTypeFormatted: String,
)

/**
 * Represents a relationship between this manga and another manga.
 *
 * @property node The related manga node.
 * @property relationType The type of relationship.
 * @property relationTypeFormatted The formatted name of the relationship type.
 */
public data class RelatedManga(
    val node: MangaNode,
    val relationType: String,
    val relationTypeFormatted: String,
)

/**
 * A simplified manga node used in relationships and recommendations.
 *
 * @property id The unique identifier for the manga.
 * @property title The title of the manga.
 * @property mainPicture The main picture of the manga.
 */
public data class MangaNode(
    val id: Int,
    val title: String,
    val mainPicture: Picture? = null,
)

/**
 * Represents a recommendation for this manga.
 *
 * @property node The recommended manga node.
 * @property numRecommendations The number of users who recommended this.
 */
public data class Recommendation(
    val node: MangaNode,
    val numRecommendations: Int,
)

/**
 * Represents a serialization of the manga.
 *
 * @property node The serialization node details.
 * @property role The role.
 */
public data class Serialization(
    val node: SerializationNode,
    val role: String? = null,
)

/**
 * Represents a serialization node (e.g., a magazine).
 *
 * @property id The unique identifier for the serialization.
 * @property name The name of the serialization.
 */
public data class SerializationNode(
    val id: Int,
    val name: String,
)

/**
 * The media type of a manga.
 */
public enum class MediaType {
    /** Unknown media type. */
    Unknown,
    /** Manga. */
    Manga,
    /** Novel. */
    Novel,
    /** One-shot manga. */
    OneShot,
    /** Doujinshi. */
    Doujinshi,
    /** Manhwa. */
    Manhwa,
    /** Manhua. */
    Manhua,
    /** Original English-language manga. */
    Oel
}

/**
 * The publishing status of a manga.
 */
public enum class MangaStatus {
    /** The manga has finished publishing. */
    Finished,
    /** The manga is currently being published. */
    CurrentlyPublishing,
    /** The manga is on hiatus. */
    OnHiatus,
    /** The manga has been discontinued. */
    Discontinued,
    /** The manga has not yet been published. */
    NotYetPublished
}

/**
 * The type of manga ranking.
 */
public enum class MangaRankingType(internal val value: String) {
    /**
     * Top Manga Series.
     */
    All("all"),
    /**
     * Top Manga.
     */
    Manga("manga"),
    /**
     * Top Novels.
     */
    Novels("novels"),
    /**
     * Top One-shots.
     */
    OneShots("oneshots"),
    /**
     * Top Doujinshi.
     */
    Doujinshi("doujin"),
    /**
     * Top Manhwa.
     */
    Manhwa("manhwa"),
    /**
     * Top Manhua.
     */
    Manhua("manhua"),
    /**
     * Top Manga by Popularity.
     */
    ByPopularity("bypopularity"),
    /**
     * Top Favorite Manga.
     */
    Favorite("favorite")
}

/**
 * Sorting options for a user's manga list.
 */
public enum class UserMangaListSortOption(internal val value: String) {
    /** Sort by the score in the user's list. */
    Score("list_score"),
    /** Sort by the last update time in the user's list. */
    UpdatedAt("list_updated_at"),
    /** Sort by manga title. */
    Title("manga_title"),
    /** Sort by manga start date. */
    StartDate("manga_start_date"),
}
