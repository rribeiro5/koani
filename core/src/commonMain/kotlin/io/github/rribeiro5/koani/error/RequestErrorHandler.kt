package io.github.rribeiro5.koani.error

import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse

private const val REDIRECT_MIN = 300
private const val REDIRECT_MAX = 399
private const val BAD_REQUEST = 400
private const val UNAUTHORIZED = 401
private const val FORBIDDEN = 403
private const val NOT_FOUND = 404
private const val SERVER_ERROR_MIN = 500
private const val SERVER_ERROR_MAX = 599

internal suspend fun handleRequestException(cause: Throwable) {
    if (cause !is ResponseException) throw cause

    val response = cause.response
    val statusCode = response.status.value

    val errorDto = response.safeErrorBody()

    when (statusCode) {
        in REDIRECT_MIN..REDIRECT_MAX -> throw RedirectResponseException(statusCode)
        BAD_REQUEST -> throw BadRequestException(errorDto?.error, errorDto?.message)
        UNAUTHORIZED -> throw UnauthorizedException(errorDto?.error, errorDto?.message)
        FORBIDDEN -> throw ForbiddenException(errorDto?.error, errorDto?.message)
        NOT_FOUND -> throw NotFoundException(errorDto?.error, errorDto?.message)
        in SERVER_ERROR_MIN..SERVER_ERROR_MAX -> throw ServerResponseException(statusCode)
        else -> throw cause
    }
}

private suspend fun HttpResponse.safeErrorBody(): ErrorDto? = 
    runCatching { body<ErrorDto>() }.getOrNull()
