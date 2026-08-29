package io.github.rribeiro5.koani.sample.compose

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable data object List : Route
    @Serializable data class AnimeDetail(val id: Int) : Route
    @Serializable data class MangaDetail(val id: Int) : Route
}
