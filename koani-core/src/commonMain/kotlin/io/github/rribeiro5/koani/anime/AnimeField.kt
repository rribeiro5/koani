package io.github.rribeiro5.koani.anime

/**
 * Represents the optional fields that can be requested when retrieving anime information.
 */
public enum class AnimeField(internal val fieldName: String) {
    /** Represents the id field in the API response. */
    ID("id"),

    /** Represents the title field in the API response. */
    TITLE("title"),

    /** Represents the main_picture field in the API response. */
    MAIN_PICTURE("main_picture"),

    /** Represents the alternative_titles field in the API response. */
    ALTERNATIVE_TITLES("alternative_titles"),

    /** Represents the start_date field in the API response. */
    START_DATE("start_date"),

    /** Represents the end_date field in the API response. */
    END_DATE("end_date"),

    /** Represents the synopsis field in the API response. */
    SYNOPSIS("synopsis"),

    /** Represents the mean field in the API response. */
    MEAN("mean"),

    /** Represents the rank field in the API response. */
    RANK("rank"),

    /** Represents the popularity field in the API response. */
    POPULARITY("popularity"),

    /** Represents the num_list_users field in the API response. */
    NUM_LIST_USERS("num_list_users"),

    /** Represents the num_scoring_users field in the API response. */
    NUM_SCORING_USERS("num_scoring_users"),

    /** Represents the nsfw field in the API response. */
    NSFW("nsfw"),

    /** Represents the created_at field in the API response. */
    CREATED_AT("created_at"),

    /** Represents the updated_at field in the API response. */
    UPDATED_AT("updated_at"),

    /** Represents the media_type field in the API response. */
    MEDIA_TYPE("media_type"),

    /** Represents the status field in the API response. */
    STATUS("status"),

    /** Represents the genres field in the API response. */
    GENRES("genres"),

    /** Represents the my_list_status field in the API response. */
    MY_LIST_STATUS("my_list_status"),

    /** Represents the num_episodes field in the API response. */
    NUM_EPISODES("num_episodes"),

    /** Represents the start_season field in the API response. */
    START_SEASON("start_season"),

    /** Represents the broadcast field in the API response. */
    BROADCAST("broadcast"),

    /** Represents the source field in the API response. */
    SOURCE("source"),

    /** Represents the average_episode_duration field in the API response. */
    AVERAGE_EPISODE_DURATION("average_episode_duration"),

    /** Represents the rating field in the API response. */
    RATING("rating"),

    /** Represents the pictures field in the API response. */
    PICTURES("pictures"),

    /** Represents the background field in the API response. */
    BACKGROUND("background"),

    /** Represents the related_anime field in the API response. */
    RELATED_ANIME("related_anime"),

    /** Represents the related_manga field in the API response. */
    RELATED_MANGA("related_manga"),

    /** Represents the recommendations field in the API response. */
    RECOMMENDATIONS("recommendations"),

    /** Represents the studios field in the API response. */
    STUDIOS("studios"),

    /** Represents the statistics field in the API response. */
    STATISTICS("statistics")
}
