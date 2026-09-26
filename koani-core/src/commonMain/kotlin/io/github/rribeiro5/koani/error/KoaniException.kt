package io.github.rribeiro5.koani.error

/**
 * Base class for all exceptions thrown by the Koani library.
 *
 * @param message The error message.
 */
public sealed class KoaniException(message: String) : Exception(message)

/**
 * Exception thrown when a 400 Bad Request response is received from the API.
 *
 * @property error The error code returned by the API.
 * @property apiMessage The error message returned by the API.
 */
public class BadRequestException(public val error: String?, public val apiMessage: String?) : 
    KoaniException(apiMessage ?: "Bad Request")

/**
 * Exception thrown when a 401 Unauthorized response is received from the API.
 *
 * @property error The error code returned by the API.
 * @property apiMessage The error message returned by the API.
 */
public class UnauthorizedException(public val error: String?, public val apiMessage: String?) : 
    KoaniException(apiMessage ?: "Unauthorized")

/**
 * Exception thrown when a 403 Forbidden response is received from the API.
 *
 * @property error The error code returned by the API.
 * @property apiMessage The error message returned by the API.
 */
public class ForbiddenException(public val error: String?, public val apiMessage: String?) : 
    KoaniException(apiMessage ?: "Forbidden")

/**
 * Exception thrown when a 404 Not Found response is received from the API.
 *
 * @property error The error code returned by the API.
 * @property apiMessage The error message returned by the API.
 */
public class NotFoundException(public val error: String?, public val apiMessage: String?) : 
    KoaniException(apiMessage ?: "Not Found")

/**
 * Exception thrown when a redirection error occurs.
 *
 * @property statusCode The HTTP status code.
 */
public class RedirectResponseException(public val statusCode: Int) : 
    KoaniException("Redirect error: $statusCode")

/**
 * Exception thrown when a 5xx Server Error response is received from the API.
 *
 * @property statusCode The HTTP status code.
 */
public class ServerResponseException(public val statusCode: Int) :
    KoaniException("Server error: $statusCode")
