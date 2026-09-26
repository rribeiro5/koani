package io.github.rribeiro5.koani.user.service

import io.github.rribeiro5.koani.user.dto.UserResponse

internal interface UserService {
    suspend fun getUserDetails(
        userName: String,
        fields: List<String>? = null,
    ): UserResponse
}
