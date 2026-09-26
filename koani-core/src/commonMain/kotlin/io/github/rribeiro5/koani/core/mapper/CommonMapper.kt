package io.github.rribeiro5.koani.core.mapper

import io.github.rribeiro5.koani.core.AlternativeTitles
import io.github.rribeiro5.koani.core.Genre
import io.github.rribeiro5.koani.core.Nsfw
import io.github.rribeiro5.koani.core.Picture
import io.github.rribeiro5.koani.core.dto.AlternativeTitlesResponse
import io.github.rribeiro5.koani.core.dto.GenreResponse
import io.github.rribeiro5.koani.core.dto.PictureResponse

internal fun PictureResponse.toDomain(): Picture = Picture(
    medium = medium,
    large = large
)

internal fun AlternativeTitlesResponse.toDomain(): AlternativeTitles = AlternativeTitles(
    synonyms = synonyms,
    en = en,
    ja = ja
)

internal fun GenreResponse.toDomain(): Genre = Genre(
    id = id,
    name = name
)

internal fun String.toNsfw(): Nsfw? = when (this) {
    "white" -> Nsfw.White
    "gray" -> Nsfw.Gray
    "black" -> Nsfw.Black
    else -> null
}
