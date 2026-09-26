package io.github.rribeiro5.koani.core.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PaginatedListResponse<T>(
    @SerialName("data")
    val data: List<T>,
    @SerialName("paging")
    val paging: PagingDataResponse? = null,
)

@Serializable
internal data class PagingDataResponse(
    @SerialName("next")
    val next: String? = null,
    @SerialName("previous")
    val previous: String? = null,
)
