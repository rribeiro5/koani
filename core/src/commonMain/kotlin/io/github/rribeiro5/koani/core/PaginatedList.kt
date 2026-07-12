package io.github.rribeiro5.koani.core

public data class PaginatedList<T>(
    val data: List<T>,
    val paging: PagingData
)

public data class PagingData(
    val nextUrl: String? = null,
    val previousUrl: String? = null,
    val nextOffset: Long? = null,
    val previousOffset: Long? = null,
    val limit: Long? = null
)
