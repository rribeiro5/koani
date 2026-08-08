package io.github.rribeiro5.koani.anime.mapper

import io.github.rribeiro5.koani.anime.Anime
import io.github.rribeiro5.koani.anime.AnimeNode
import io.github.rribeiro5.koani.anime.AnimeStatus
import io.github.rribeiro5.koani.anime.Broadcast
import io.github.rribeiro5.koani.anime.DayOfWeek
import io.github.rribeiro5.koani.anime.MediaType
import io.github.rribeiro5.koani.anime.MyListStatusType
import io.github.rribeiro5.koani.anime.RankedAnime
import io.github.rribeiro5.koani.anime.Rating
import io.github.rribeiro5.koani.anime.Recommendation
import io.github.rribeiro5.koani.anime.RelatedAnime
import io.github.rribeiro5.koani.anime.RelatedManga
import io.github.rribeiro5.koani.anime.Season
import io.github.rribeiro5.koani.anime.Source
import io.github.rribeiro5.koani.anime.StartSeason
import io.github.rribeiro5.koani.anime.Statistics
import io.github.rribeiro5.koani.anime.StatisticsStatus
import io.github.rribeiro5.koani.anime.Studio
import io.github.rribeiro5.koani.anime.UserAnimeListStatus
import io.github.rribeiro5.koani.anime.dto.AnimeNodeResponse
import io.github.rribeiro5.koani.anime.dto.AnimeRankingEdgeResponse
import io.github.rribeiro5.koani.anime.dto.AnimeResponse
import io.github.rribeiro5.koani.anime.dto.BroadcastResponse
import io.github.rribeiro5.koani.anime.dto.RecommendationResponse
import io.github.rribeiro5.koani.anime.dto.RelatedAnimeResponse
import io.github.rribeiro5.koani.anime.dto.RelatedMangaResponse
import io.github.rribeiro5.koani.anime.dto.StartSeasonResponse
import io.github.rribeiro5.koani.anime.dto.StatisticsResponse
import io.github.rribeiro5.koani.anime.dto.StatisticsStatusResponse
import io.github.rribeiro5.koani.anime.dto.StudioResponse
import io.github.rribeiro5.koani.anime.dto.UserAnimeListStatusResponse
import io.github.rribeiro5.koani.core.AlternativeTitles
import io.github.rribeiro5.koani.core.Genre
import io.github.rribeiro5.koani.core.Nsfw
import io.github.rribeiro5.koani.core.Picture
import io.github.rribeiro5.koani.core.dto.AlternativeTitlesResponse
import io.github.rribeiro5.koani.core.dto.GenreResponse
import io.github.rribeiro5.koani.core.dto.PictureResponse
import io.github.rribeiro5.koani.core.mapper.toDate
import io.github.rribeiro5.koani.core.mapper.toDateTime
import io.github.rribeiro5.koani.core.mapper.toDomain
import io.github.rribeiro5.koani.core.mapper.toNsfw
import io.github.rribeiro5.koani.manga.MangaNode
import io.github.rribeiro5.koani.manga.dto.MangaNodeResponse
import kotlin.time.Instant

internal fun AnimeResponse.toDomain(): Anime = Anime(
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
    status = status?.toAnimeStatus(),
    genres = genres?.map { it.toDomain() },
    myListStatus = myListStatus?.toDomain(),
    numEpisodes = numEpisodes,
    startSeason = startSeason?.toDomain(),
    broadcast = broadcast?.toDomain(),
    source = source?.toSource(),
    averageEpisodeDuration = averageEpisodeDuration,
    rating = rating?.toRating(),
    pictures = pictures?.map { it.toDomain() },
    background = background,
    relatedAnime = relatedAnime?.map { it.toDomain() },
    relatedManga = relatedManga?.map { it.toDomain() },
    recommendations = recommendations?.map { it.toDomain() },
    studios = studios?.map { it.toDomain() },
    statistics = statistics?.toDomain()
)

internal fun UserAnimeListStatusResponse.toDomain(): UserAnimeListStatus = UserAnimeListStatus(
    status = status.toMyListStatusType(),
    score = score,
    numEpisodesWatched = numEpisodesWatched,
    isRewatching = isRewatching,
    updatedAt = updatedAt.toDateTime() ?: Instant.fromEpochMilliseconds(0),
    startDate = startDate?.toDate(),
    finishDate = finishDate?.toDate(),
    priority = priority,
    numTimesRewatched = numTimesRewatched,
    rewatchValue = rewatchValue,
    tags = tags ?: emptyList(),
    comments = comments
)

internal fun StartSeasonResponse.toDomain(): StartSeason = StartSeason(
    year = year,
    season = season.toSeason()
)

internal fun BroadcastResponse.toDomain(): Broadcast = Broadcast(
    dayOfTheWeek = dayOfTheWeek.toDayOfWeek(),
    startTime = startTime
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

internal fun RecommendationResponse.toDomain(): Recommendation = Recommendation(
    node = node.toDomain(),
    numRecommendations = numRecommendations
)

internal fun StudioResponse.toDomain(): Studio = Studio(
    id = id,
    name = name
)

internal fun StatisticsResponse.toDomain(): Statistics = Statistics(
    status = status.toDomain(),
    numListUsers = numListUsers
)

internal fun StatisticsStatusResponse.toDomain(): StatisticsStatus = StatisticsStatus(
    watching = watching,
    completed = completed,
    onHold = onHold,
    dropped = dropped,
    planToWatch = planToWatch
)

internal fun AnimeNodeResponse.toDomain(): AnimeNode = AnimeNode(
    id = id,
    title = title,
    mainPicture = mainPicture?.toDomain()
)

internal fun AnimeRankingEdgeResponse.toDomain(): RankedAnime = RankedAnime(
    anime = node.toDomain(),
    rank = ranking.rank,
    previousRank = ranking.previousRank
)

internal fun MangaNodeResponse.toDomain(): MangaNode = MangaNode(
    id = id,
    title = title,
    mainPicture = mainPicture?.toDomain()
)

private fun String.toMediaType(): MediaType? = when (this) {
    "tv" -> MediaType.Tv
    "ova" -> MediaType.Ova
    "movie" -> MediaType.Movie
    "special" -> MediaType.Special
    "ona" -> MediaType.Ona
    "music" -> MediaType.Music
    "unknown" -> MediaType.Unknown
    else -> null
}

private fun String.toAnimeStatus(): AnimeStatus? = when (this) {
    "finished_airing" -> AnimeStatus.FinishedAiring
    "currently_airing" -> AnimeStatus.CurrentlyAiring
    "not_yet_aired" -> AnimeStatus.NotYetAired
    else -> null
}

private fun String.toSeason(): Season = when (this) {
    "winter" -> Season.Winter
    "spring" -> Season.Spring
    "summer" -> Season.Summer
    "fall" -> Season.Fall
    else -> Season.Unknown
}

private fun String.toRating(): Rating? = when (this) {
    "g" -> Rating.G
    "pg" -> Rating.Pg
    "pg_13" -> Rating.Pg13
    "r" -> Rating.R
    "r+" -> Rating.RPlus
    "rx" -> Rating.Rx
    else -> null
}

private fun String.toMyListStatusType(): MyListStatusType = when (this) {
    "watching" -> MyListStatusType.Watching
    "completed" -> MyListStatusType.Completed
    "on_hold" -> MyListStatusType.OnHold
    "dropped" -> MyListStatusType.Dropped
    "plan_to_watch" -> MyListStatusType.PlanToWatch
    else -> MyListStatusType.Unknown
}

private fun String.toSource(): Source? = when (this) {
    "original" -> Source.Original
    "manga" -> Source.Manga
    "4_koma_manga" -> Source.FourKomaManga
    "web_manga" -> Source.WebManga
    "digital_manga" -> Source.DigitalManga
    "novel" -> Source.Novel
    "light_novel" -> Source.LightNovel
    "visual_novel" -> Source.VisualNovel
    "game" -> Source.Game
    "video_game" -> Source.VideoGame
    "card_game" -> Source.CardGame
    "book" -> Source.Book
    "picture_book" -> Source.PictureBook
    "radio" -> Source.Radio
    "music" -> Source.Music
    "other" -> Source.Other
    else -> null
}

private fun String.toDayOfWeek(): DayOfWeek? = when (this) {
    "monday" -> DayOfWeek.Monday
    "tuesday" -> DayOfWeek.Tuesday
    "wednesday" -> DayOfWeek.Wednesday
    "thursday" -> DayOfWeek.Thursday
    "friday" -> DayOfWeek.Friday
    "saturday" -> DayOfWeek.Saturday
    "sunday" -> DayOfWeek.Sunday
    "other" -> DayOfWeek.Other
    else -> null
}
