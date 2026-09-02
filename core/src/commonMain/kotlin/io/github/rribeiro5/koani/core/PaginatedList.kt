package io.github.rribeiro5.koani.core

/**
 * Represents a paginated list of items.
 *
 * @param T The type of items in the list.
 * @property data The list of items in the current page.
 * @property paging The paging information for navigating between pages.
 */
public data class PaginatedList<T>(
    val data: List<T>,
    val paging: PagingData
)

/**
 * Represents paging information for a paginated list.
 *
 * @property nextUrl The URL for the next page of results.
 * @property previousUrl The URL for the previous page of results.
 * @property nextOffset The offset for the next page of results.
 * @property previousOffset The offset for the previous page of results.
 * @property limit The maximum number of results per page.
 */
public data class PagingData(
    val nextUrl: String? = null,
    val previousUrl: String? = null,
    val nextOffset: Long? = null,
    val previousOffset: Long? = null,
    val limit: Long? = null
)
