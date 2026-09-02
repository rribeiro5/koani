package io.github.rribeiro5.koani.user

/**
 * Represents the optional fields that can be requested when retrieving user information.
 */
public enum class UserField(internal val fieldName: String) {
    /** Represents the id field in the API response. */
    ID("id"),

    /** Represents the name field in the API response. */
    NAME("name"),

    /** Represents the picture field in the API response. */
    PICTURE("picture"),

    /** Represents the gender field in the API response. */
    GENDER("gender"),

    /** Represents the birthday field in the API response. */
    BIRTHDAY("birthday"),

    /** Represents the location field in the API response. */
    LOCATION("location"),

    /** Represents the joined_at field in the API response. */
    JOINED_AT("joined_at"),

    /** Represents the anime_statistics field in the API response. */
    ANIME_STATISTICS("anime_statistics"),

    /** Represents the time_zone field in the API response. */
    TIME_ZONE("time_zone"),

    /** Represents the is_supporter field in the API response. */
    IS_SUPPORTER("is_supporter")
}
