package io.github.rribeiro5.koani.manga.dto

import io.github.rribeiro5.koani.anime.dto.AnimeNodeResponse
import io.github.rribeiro5.koani.core.dto.AlternativeTitlesResponse
import io.github.rribeiro5.koani.core.dto.GenreResponse
import io.github.rribeiro5.koani.core.dto.PictureResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MangaResponse(
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
    val myListStatus: UserMangaListStatusResponse? = null,
    @SerialName("num_volumes")
    val numVolumes: Int? = null,
    @SerialName("num_chapters")
    val numChapters: Int? = null,
    @SerialName("authors")
    val authors: List<AuthorResponse>? = null,
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
    @SerialName("serialization")
    val serialization: List<SerializationResponse>? = null,
)

@Serializable
internal data class UserMangaListStatusResponse(
    @SerialName("status")
    val status: String,
    @SerialName("score")
    val score: Int,
    @SerialName("num_volumes_read")
    val numVolumesRead: Int,
    @SerialName("num_chapters_read")
    val numChaptersRead: Int,
    @SerialName("is_rereading")
    val isRereading: Boolean,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("finish_date")
    val finishDate: String? = null,
    @SerialName("priority")
    val priority: Int? = null,
    @SerialName("num_times_reread")
    val numTimesReread: Int? = null,
    @SerialName("reread_value")
    val rereadValue: Int? = null,
    @SerialName("tags")
    val tags: List<String>? = null,
    @SerialName("comments")
    val comments: String? = null,
)

@Serializable
internal data class AuthorResponse(
    @SerialName("node")
    val node: AuthorNodeResponse,
    @SerialName("role")
    val role: String? = null,
)

@Serializable
internal data class AuthorNodeResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("last_name")
    val lastName: String? = null,
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
internal data class MangaNodeResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("main_picture")
    val mainPicture: PictureResponse? = null,
)

@Serializable
internal data class RecommendationResponse(
    @SerialName("node")
    val node: MangaNodeResponse,
    @SerialName("num_recommendations")
    val numRecommendations: Int,
)

@Serializable
internal data class SerializationResponse(
    @SerialName("node")
    val node: SerializationNodeResponse,
    @SerialName("role")
    val role: String? = null,
)

@Serializable
internal data class SerializationNodeResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)
