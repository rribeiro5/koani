package io.github.rribeiro5.koani.user

public enum class UserField(internal val fieldName: String) {
    ID("id"),
    NAME("name"),
    PICTURE("picture"),
    GENDER("gender"),
    BIRTHDAY("birthday"),
    LOCATION("location"),
    JOINED_AT("joined_at"),
    ANIME_STATISTICS("anime_statistics"),
    TIME_ZONE("time_zone"),
    IS_SUPPORTER("is_supporter")
}
