package io.github.rribeiro5.koani.anime.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AnimeResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("main_picture")
    val mainPicture: PictureResponse? = null,
    @SerialName("alternative_titles")
    val alternativeTitles: AlternativeTitlesResponse? = null,
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("end_date")
    val endDate: String? = null,
    @SerialName("synopsis")
    val synopsis: String? = null,
    @SerialName("mean")
    val mean: Float? = null,
    @SerialName("rank")
    val rank: Int? = null,
    @SerialName("popularity")
    val popularity: Int? = null,
    @SerialName("num_list_users")
    val numListUsers: Int? = null,
    @SerialName("num_scoring_users")
    val numScoringUsers: Int? = null,
    @SerialName("nsfw")
    val nsfw: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    @SerialName("media_type")
    val mediaType: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("genres")
    val genres: List<GenreResponse>? = null,
    @SerialName("my_list_status")
    val myListStatus: MyListStatusResponse? = null,
    @SerialName("num_episodes")
    val numEpisodes: Int? = null,
    @SerialName("start_season")
    val startSeason: StartSeasonResponse? = null,
    @SerialName("broadcast")
    val broadcast: BroadcastResponse? = null,
    @SerialName("source")
    val source: String? = null,
    @SerialName("average_episode_duration")
    val averageEpisodeDuration: Int? = null,
    @SerialName("rating")
    val rating: String? = null,
    @SerialName("pictures")
    val pictures: List<PictureResponse>? = null,
    @SerialName("background")
    val background: String? = null,
    @SerialName("related_anime")
    val relatedAnime: List<RelatedAnimeResponse>? = null,
    @SerialName("related_manga")
    val relatedManga: List<RelatedMangaResponse>? = null,
    @SerialName("recommendations")
    val recommendations: List<RecommendationResponse>? = null,
    @SerialName("studios")
    val studios: List<StudioResponse>? = null,
    @SerialName("statistics")
    val statistics: StatisticsResponse? = null,
)

@Serializable
internal data class PictureResponse(
    @SerialName("medium")
    val medium: String,
    @SerialName("large")
    val large: String? = null,
)

@Serializable
internal data class AlternativeTitlesResponse(
    @SerialName("synonyms")
    val synonyms: List<String> = emptyList(),
    @SerialName("en")
    val en: String? = null,
    @SerialName("ja")
    val ja: String? = null,
)

@Serializable
internal data class GenreResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

@Serializable
internal data class MyListStatusResponse(
    @SerialName("status")
    val status: String,
    @SerialName("score")
    val score: Int,
    @SerialName("num_episodes_watched")
    val numEpisodesWatched: Int,
    @SerialName("is_rewatching")
    val isRewatching: Boolean,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("finish_date")
    val finishDate: String? = null,
    @SerialName("priority")
    val priority: Int? = null,
    @SerialName("num_times_rewatched")
    val numTimesRewatched: Int? = null,
    @SerialName("rewatch_value")
    val rewatchValue: Int? = null,
    @SerialName("tags")
    val tags: List<String>? = null,
    @SerialName("comments")
    val comments: String? = null,
)

@Serializable
internal data class StartSeasonResponse(
    @SerialName("year")
    val year: Int,
    @SerialName("season")
    val season: String,
)

@Serializable
internal data class BroadcastResponse(
    @SerialName("day_of_the_week")
    val dayOfTheWeek: String,
    @SerialName("start_time")
    val startTime: String? = null,
)

@Serializable
internal data class RelatedAnimeResponse(
    @SerialName("node")
    val node: AnimeNodeResponse,
    @SerialName("relation_type")
    val relationType: String,
    @SerialName("relation_type_formatted")
    val relationTypeFormatted: String,
)

@Serializable
internal data class RelatedMangaResponse(
    @SerialName("node")
    val node: MangaNodeResponse,
    @SerialName("relation_type")
    val relationType: String,
    @SerialName("relation_type_formatted")
    val relationTypeFormatted: String,
)

@Serializable
internal data class RecommendationResponse(
    @SerialName("node")
    val node: AnimeNodeResponse,
    @SerialName("num_recommendations")
    val numRecommendations: Int,
)

@Serializable
internal data class StudioResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

@Serializable
internal data class StatisticsResponse(
    @SerialName("status")
    val status: StatisticsStatusResponse,
    @SerialName("num_list_users")
    val numListUsers: Int,
)

@Serializable
internal data class StatisticsStatusResponse(
    @SerialName("watching")
    val watching: Int,
    @SerialName("completed")
    val completed: Int,
    @SerialName("on_hold")
    val onHold: Int,
    @SerialName("dropped")
    val dropped: Int,
    @SerialName("plan_to_watch")
    val planToWatch: Int,
)

@Serializable
internal data class AnimeNodeResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("main_picture")
    val mainPicture: PictureResponse? = null,
)

@Serializable
internal data class MangaNodeResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("main_picture")
    val mainPicture: PictureResponse? = null,
)
