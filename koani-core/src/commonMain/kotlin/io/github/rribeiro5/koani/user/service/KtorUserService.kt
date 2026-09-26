package io.github.rribeiro5.koani.user.service

import io.github.rribeiro5.koani.user.dto.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class KtorUserService(
    private val httpClient: HttpClient,
) : UserService {

    override suspend fun getUserDetails(
        userName: String,
        fields: List<String>?,
    ): UserResponse = httpClient.get("v2/users/$userName") {
        fields?.let { parameter("fields", it.joinToString(",")) }
    }.body()
}
