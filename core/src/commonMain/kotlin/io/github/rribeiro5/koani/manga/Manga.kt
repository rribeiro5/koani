package io.github.rribeiro5.koani.manga

import io.github.rribeiro5.koani.anime.AnimeNode
import io.github.rribeiro5.koani.core.AlternativeTitles
import io.github.rribeiro5.koani.core.Genre
import io.github.rribeiro5.koani.core.Nsfw
import io.github.rribeiro5.koani.core.Picture
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

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

public data class RankedManga(
    val manga: Manga,
    val rank: Int,
    val previousRank: Int? = null,
)

public data class UserMangaListItem(
    val manga: Manga,
    val listStatus: UserMangaListStatus,
)

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

public enum class UserMangaListStatusType {
    Unknown, Reading, Completed, OnHold, Dropped, PlanToRead
}

public data class Author(
    val node: AuthorNode,
    val role: String? = null,
)

public data class AuthorNode(
    val id: Int,
    val firstName: String? = null,
    val lastName: String? = null,
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

public data class MangaNode(
    val id: Int,
    val title: String,
    val mainPicture: Picture? = null,
)

public data class Recommendation(
    val node: MangaNode,
    val numRecommendations: Int,
)

public data class Serialization(
    val node: SerializationNode,
    val role: String? = null,
)

public data class SerializationNode(
    val id: Int,
    val name: String,
)

public enum class MediaType {
    Unknown, Manga, Novel, OneShot, Doujinshi, Manhwa, Manhua, Oel
}

public enum class MangaStatus {
    Finished, CurrentlyPublishing, OnHiatus, Discontinued, NotYetPublished
}

public enum class MangaRankingType(internal val value: String) {
    All("all"),
    Manga("manga"),
    Novels("novels"),
    OneShots("oneshots"),
    Doujinshi("doujin"),
    Manhwa("manhwa"),
    Manhua("manhua"),
    ByPopularity("bypopularity"),
    Favorite("favorite")
}

public enum class UserMangaListSortOption(internal val value: String) {
    Score("list_score"),
    UpdatedAt("list_updated_at"),
    Title("manga_title"),
    StartDate("manga_start_date"),
}
