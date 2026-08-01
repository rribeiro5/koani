package io.github.rribeiro5.koani.core.mapper

import io.github.rribeiro5.koani.core.PaginatedList
import io.github.rribeiro5.koani.core.PagingData
import io.github.rribeiro5.koani.core.dto.PaginatedListResponse
import io.github.rribeiro5.koani.core.dto.PagingDataResponse
import io.ktor.http.Url

internal fun <T, R> PaginatedListResponse<T>.toPaginatedList(
    mapItem: (T) -> R
): PaginatedList<R> = PaginatedList(
    data = data.map(mapItem),
    paging = paging.toPagingData()
)

internal fun <T, R> PaginatedListResponse<T>.flatMap(
    mapItem: (T) -> R
): PaginatedListResponse<R> = PaginatedListResponse(
    data = data.map(mapItem),
    paging = paging
)

internal fun PagingDataResponse?.toPagingData(): PagingData {
    val nextParsedUrl = this?.next?.let { Url(it) }
    val previousParsedUrl = this?.previous?.let { Url(it) }

    return PagingData(
        nextUrl = this?.next,
        previousUrl = this?.previous,
        nextOffset = nextParsedUrl?.parameters?.get("offset")?.toLongOrNull(),
        previousOffset = previousParsedUrl?.parameters?.get("offset")?.toLongOrNull(),
        limit = nextParsedUrl?.parameters?.get("limit")?.toLongOrNull()
            ?: previousParsedUrl?.parameters?.get("limit")?.toLongOrNull()
    )
}
