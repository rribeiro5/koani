package io.github.rribeiro5.koani.manga.mapper

import io.github.rribeiro5.koani.anime.AnimeNode
import io.github.rribeiro5.koani.anime.dto.AnimeNodeResponse
import io.github.rribeiro5.koani.core.AlternativeTitles
import io.github.rribeiro5.koani.core.Genre
import io.github.rribeiro5.koani.core.Nsfw
import io.github.rribeiro5.koani.core.Picture
import io.github.rribeiro5.koani.core.dto.AlternativeTitlesResponse
import io.github.rribeiro5.koani.core.dto.GenreResponse
import io.github.rribeiro5.koani.core.dto.PictureResponse
import io.github.rribeiro5.koani.core.mapper.toDomain
import io.github.rribeiro5.koani.core.mapper.toNsfw
import io.github.rribeiro5.koani.core.mapper.toDate
import io.github.rribeiro5.koani.core.mapper.toDateTime
import io.github.rribeiro5.koani.manga.Author
import io.github.rribeiro5.koani.manga.AuthorNode
import io.github.rribeiro5.koani.manga.Manga
import io.github.rribeiro5.koani.manga.MangaListStatusType
import io.github.rribeiro5.koani.manga.MediaType
import io.github.rribeiro5.koani.manga.UserMangaListStatus
import io.github.rribeiro5.koani.manga.MangaStatus
import io.github.rribeiro5.koani.manga.MangaNode
import io.github.rribeiro5.koani.manga.RankedManga
import io.github.rribeiro5.koani.manga.Recommendation
import io.github.rribeiro5.koani.manga.RelatedAnime
import io.github.rribeiro5.koani.manga.RelatedManga
import io.github.rribeiro5.koani.manga.Serialization
import io.github.rribeiro5.koani.manga.SerializationNode
import io.github.rribeiro5.koani.manga.dto.AuthorNodeResponse
import io.github.rribeiro5.koani.manga.dto.AuthorResponse
import io.github.rribeiro5.koani.manga.dto.UserMangaListStatusResponse
import io.github.rribeiro5.koani.manga.dto.MangaNodeResponse
import io.github.rribeiro5.koani.manga.dto.MangaRankingEdgeResponse
import io.github.rribeiro5.koani.manga.dto.MangaResponse
import io.github.rribeiro5.koani.manga.dto.RecommendationResponse
import io.github.rribeiro5.koani.manga.dto.RelatedAnimeResponse
import io.github.rribeiro5.koani.manga.dto.RelatedMangaResponse
import io.github.rribeiro5.koani.manga.dto.SerializationNodeResponse
import io.github.rribeiro5.koani.manga.dto.SerializationResponse
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

internal fun MangaResponse.toDomain(): Manga = Manga(
    id = id,
    title = title,
    mainPicture = mainPicture?.toDomain(),
    alternativeTitles = alternativeTitles?.toDomain(),
    startDate = startDate?.toDate(),
    endDate = endDate?.toDate(),
    synopsis = synopsis,
    mean = mean,
    rank = rank,
    popularity = popularity,
    numListUsers = numListUsers,
    numScoringUsers = numScoringUsers,
    nsfw = nsfw?.toNsfw(),
    createdAt = createdAt?.toDateTime(),
    updatedAt = updatedAt?.toDateTime(),
    mediaType = mediaType?.toMediaType(),
    status = status?.toMangaStatus(),
    genres = genres?.map { it.toDomain() },
    myListStatus = myListStatus?.toDomain(),
    numVolumes = numVolumes,
    numChapters = numChapters,
    authors = authors?.map { it.toDomain() },
    pictures = pictures?.map { it.toDomain() },
    background = background,
    relatedAnime = relatedAnime?.map { it.toDomain() },
    relatedManga = relatedManga?.map { it.toDomain() },
    recommendations = recommendations?.map { it.toDomain() },
    serialization = serialization?.map { it.toDomain() }
)

internal fun UserMangaListStatusResponse.toDomain(): UserMangaListStatus = UserMangaListStatus(
    status = status.toMangaListStatusType(),
    score = score,
    numVolumesRead = numVolumesRead,
    numChaptersRead = numChaptersRead,
    isRereading = isRereading,
    updatedAt = updatedAt.toDateTime() ?: Instant.fromEpochMilliseconds(0),
    startDate = startDate?.toDate(),
    finishDate = finishDate?.toDate(),
    priority = priority,
    numTimesReread = numTimesReread,
    rereadValue = rereadValue,
    tags = tags ?: emptyList(),
    comments = comments
)

internal fun AuthorResponse.toDomain(): Author = Author(
    node = node.toDomain(),
    role = role
)

internal fun AuthorNodeResponse.toDomain(): AuthorNode = AuthorNode(
    id = id,
    firstName = firstName,
    lastName = lastName
)

internal fun RelatedAnimeResponse.toDomain(): RelatedAnime = RelatedAnime(
    node = node.toDomain(),
    relationType = relationType,
    relationTypeFormatted = relationTypeFormatted
)

internal fun RelatedMangaResponse.toDomain(): RelatedManga = RelatedManga(
    node = node.toDomain(),
    relationType = relationType,
    relationTypeFormatted = relationTypeFormatted
)

internal fun AnimeNodeResponse.toDomain(): AnimeNode = AnimeNode(
    id = id,
    title = title,
    mainPicture = mainPicture?.toDomain()
)

internal fun MangaNodeResponse.toDomain(): MangaNode = MangaNode(
    id = id,
    title = title,
    mainPicture = mainPicture?.toDomain()
)

internal fun RecommendationResponse.toDomain(): Recommendation = Recommendation(
    node = node.toDomain(),
    numRecommendations = numRecommendations
)

internal fun SerializationResponse.toDomain(): Serialization = Serialization(
    node = node.toDomain(),
    role = role
)

internal fun SerializationNodeResponse.toDomain(): SerializationNode = SerializationNode(
    id = id,
    name = name
)

internal fun MangaRankingEdgeResponse.toDomain(): RankedManga = RankedManga(
    manga = node.toDomain(),
    rank = ranking.rank,
    previousRank = ranking.previousRank
)

private fun String.toMediaType(): MediaType? = when (this) {
    "manga" -> MediaType.Manga
    "novel" -> MediaType.Novel
    "one_shot" -> MediaType.OneShot
    "doujinshi" -> MediaType.Doujinshi
    "manhwa" -> MediaType.Manhwa
    "manhua" -> MediaType.Manhua
    "oel" -> MediaType.Oel
    "unknown" -> MediaType.Unknown
    else -> null
}

private fun String.toMangaStatus(): MangaStatus? = when (this) {
    "finished" -> MangaStatus.Finished
    "currently_publishing" -> MangaStatus.CurrentlyPublishing
    "on_hiatus" -> MangaStatus.OnHiatus
    "discontinued" -> MangaStatus.Discontinued
    "not_yet_published" -> MangaStatus.NotYetPublished
    else -> null
}

private fun String.toMangaListStatusType(): MangaListStatusType = when (this) {
    "reading" -> MangaListStatusType.Reading
    "completed" -> MangaListStatusType.Completed
    "on_hold" -> MangaListStatusType.OnHold
    "dropped" -> MangaListStatusType.Dropped
    "plan_to_read" -> MangaListStatusType.PlanToRead
    else -> MangaListStatusType.Unknown
}
