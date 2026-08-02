package io.github.rribeiro5.koani.anime

import kotlinx.datetime.LocalDate
import kotlin.time.Instant

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
    val myListStatus: MyListStatus? = null,
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

public data class RankedAnime(
    val anime: Anime,
    val rank: Int,
    val previousRank: Int? = null,
)

public data class Picture(
    val medium: String,
    val large: String? = null,
)

public data class AlternativeTitles(
    val synonyms: List<String> = emptyList(),
    val en: String? = null,
    val ja: String? = null,
)

public data class Genre(
    val id: Int,
    val name: String,
)

public data class MyListStatus(
    val status: MyListStatusType,
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

public data class StartSeason(
    val year: Int,
    val season: Season,
)

public data class Broadcast(
    val dayOfTheWeek: DayOfWeek? = null,
    val startTime: String? = null,
)

public data class RelatedAnime(
    val node: AnimeNode,
    val relationType: String,
    val relationTypeFormatted: String,
)

public data class RelatedManga(
    val node: MangaNode,
    val relationType: String,
    val relationTypeFormatted: String,
)

public data class Recommendation(
    val node: AnimeNode,
    val numRecommendations: Int,
)

public data class Studio(
    val id: Int,
    val name: String,
)

public data class Statistics(
    val status: StatisticsStatus,
    val numListUsers: Int,
)

public data class StatisticsStatus(
    val watching: Int,
    val completed: Int,
    val onHold: Int,
    val dropped: Int,
    val planToWatch: Int,
)

public data class AnimeNode(
    val id: Int,
    val title: String,
    val mainPicture: Picture? = null,
)

public data class MangaNode(
    val id: Int,
    val title: String,
    val mainPicture: Picture? = null,
)

public enum class Nsfw {
    White, Gray, Black
}

public enum class MediaType {
    Unknown, Tv, Ova, Movie, Special, Ona, Music
}

public enum class AnimeStatus {
    FinishedAiring, CurrentlyAiring, NotYetAired
}

public enum class Season(internal val value: String) {
    Unknown("unknown"),
    Winter("winter"),
    Spring("spring"),
    Summer("summer"),
    Fall("fall")
}

public enum class Rating {
    G, Pg, Pg13, R, RPlus, Rx
}

public enum class MyListStatusType {
    Unknown, Watching, Completed, OnHold, Dropped, PlanToWatch
}

public enum class Source {
    Original, Manga, FourKomaManga, WebManga, DigitalManga, Novel, LightNovel, VisualNovel,
    Game, VideoGame, CardGame, Book, PictureBook, Radio, Music, Other
}

public enum class DayOfWeek {
    Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday, Other
}

public enum class RankingType(internal val value: String) {
    All("all"),
    Airing("airing"),
    Upcoming("upcoming"),
    Tv("tv"),
    Ova("ova"),
    Movie("movie"),
    Special("special"),
    ByPopularity("bypopularity"),
    Favorite("favorite")
}

public enum class SeasonalAnimeSort(internal val value: String) {
    AnimeScore("anime_score"),
    AnimeNumListUsers("anime_num_list_users")
}
