package io.github.rribeiro5.koani.core

/**
 * Represents a picture with different sizes.
 *
 * @property medium The URL of the medium-sized picture.
 * @property large The URL of the large-sized picture.
 */
public data class Picture(
    val medium: String,
    val large: String? = null,
)

/**
 * Represents alternative titles for a node (anime or manga).
 *
 * @property synonyms A list of title synonyms.
 * @property en The English title.
 * @property ja The Japanese title.
 */
public data class AlternativeTitles(
    val synonyms: List<String> = emptyList(),
    val en: String? = null,
    val ja: String? = null,
)

/**
 * Represents a genre.
 *
 * @property id The unique identifier for the genre.
 * @property name The name of the genre.
 */
public data class Genre(
    val id: Int,
    val name: String,
)

/**
 * Represents the NSFW (Not Safe For Work) rating.
 */
public enum class Nsfw {
    /**
     * White: Safe for work.
     */
    White,
    /**
     * Gray: May contain some suggestive content.
     */
    Gray,
    /**
     * Black: Not safe for work.
     */
    Black
}
