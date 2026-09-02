package io.github.rribeiro5.koani.manga

/**
 * Represents the optional fields that can be requested when retrieving manga information.
 */
public enum class MangaField(internal val fieldName: String) {
    ID("id"),
    TITLE("title"),
    MAIN_PICTURE("main_picture"),
    ALTERNATIVE_TITLES("alternative_titles"),
    START_DATE("start_date"),
    END_DATE("end_date"),
    SYNOPSIS("synopsis"),
    MEAN("mean"),
    RANK("rank"),
    POPULARITY("popularity"),
    NUM_LIST_USERS("num_list_users"),
    NUM_SCORING_USERS("num_scoring_users"),
    NSFW("nsfw"),
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at"),
    MEDIA_TYPE("media_type"),
    STATUS("status"),
    GENRES("genres"),
    MY_LIST_STATUS("my_list_status"),
    NUM_VOLUMES("num_volumes"),
    NUM_CHAPTERS("num_chapters"),
    AUTHORS("authors"),
    PICTURES("pictures"),
    BACKGROUND("background"),
    RELATED_ANIME("related_anime"),
    RELATED_MANGA("related_manga"),
    RECOMMENDATIONS("recommendations"),
    SERIALIZATION("serialization")
}
