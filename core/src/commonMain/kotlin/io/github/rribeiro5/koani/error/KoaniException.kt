package io.github.rribeiro5.koani.error

public sealed class KoaniException(message: String) : Exception(message)

public class BadRequestException(public val error: String?, public val apiMessage: String?) : 
    KoaniException(apiMessage ?: "Bad Request")

public class UnauthorizedException(public val error: String?, public val apiMessage: String?) : 
    KoaniException(apiMessage ?: "Unauthorized")

public class ForbiddenException(public val error: String?, public val apiMessage: String?) : 
    KoaniException(apiMessage ?: "Forbidden")

public class NotFoundException(public val error: String?, public val apiMessage: String?) : 
    KoaniException(apiMessage ?: "Not Found")

public class RedirectResponseException(public val statusCode: Int) : 
    KoaniException("Redirect error: $statusCode")

public class ServerResponseException(public val statusCode: Int) :
    KoaniException("Server error: $statusCode")
