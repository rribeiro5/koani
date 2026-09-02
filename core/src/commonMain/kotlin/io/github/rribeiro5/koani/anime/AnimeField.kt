package io.github.rribeiro5.koani.anime

/**
 * Represents the optional fields that can be requested when retrieving anime information.
 */
public enum class AnimeField(internal val fieldName: String) {
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
    NUM_EPISODES("num_episodes"),
    START_SEASON("start_season"),
    BROADCAST("broadcast"),
    SOURCE("source"),
    AVERAGE_EPISODE_DURATION("average_episode_duration"),
    RATING("rating"),
    PICTURES("pictures"),
    BACKGROUND("background"),
    RELATED_ANIME("related_anime"),
    RELATED_MANGA("related_manga"),
    RECOMMENDATIONS("recommendations"),
    STUDIOS("studios"),
    STATISTICS("statistics")
}
