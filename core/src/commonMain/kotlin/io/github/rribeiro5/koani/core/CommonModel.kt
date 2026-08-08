package io.github.rribeiro5.koani.core

public data class Picture(
    val medium: String,
    val large: String? = null,
)

public data class AlternativeTitles(
    val synonyms: List<String> = emptyList(),
    val en: String? = null,
    val ja: String? = null,
)

public data class Genre(
    val id: Int,
    val name: String,
)

public enum class Nsfw {
    White, Gray, Black
}
