package io.github.rribeiro5.koani.auth

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.awt.Desktop
import java.net.InetSocketAddress
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.SecureRandom
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Base64
import java.util.Properties
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

private const val DEFAULT_PORT = 8765
private const val CALLBACK_PATH = "/callback"
private const val AUTHORIZATION_URL = "https://myanimelist.net/v1/oauth2/authorize"
private const val TOKEN_URL = "https://myanimelist.net/v1/oauth2/token"
private const val CLIENT_ID_PROPERTY = "TEST_MAL_CLIENT_ID"
private const val CLIENT_SECRET_PROPERTY = "TEST_MAL_CLIENT_SECRET"
private const val REDIRECT_URI_PROPERTY = "TEST_MAL_REDIRECT_URI"
private const val TOKEN_WAIT_SECONDS = 300L

public fun main(args: Array<String>) {
    if (args.any { it == "--help" || it == "-h" }) {
        printHelp()
        return
    }
    val options = RotationOptions.parse(args)
    val properties = loadProperties()
    val clientId = options.clientId
        ?: propertyValue(CLIENT_ID_PROPERTY, properties)
        ?: error("Missing $CLIENT_ID_PROPERTY. Set it in local.properties or the environment.")
    val clientSecret = options.clientSecret
        ?: propertyValue(CLIENT_SECRET_PROPERTY, properties)
    val redirectUri = options.redirectUri
        ?: propertyValue(REDIRECT_URI_PROPERTY, properties)
        ?: "http://127.0.0.1:${options.port}$CALLBACK_PATH"

    require(URI(redirectUri).host == "127.0.0.1") {
        "The redirect URI must use the registered localhost host 127.0.0.1."
    }
    require(URI(redirectUri).port > 0) {
        "The redirect URI must include a valid localhost port."
    }

    val callback = CallbackServer(redirectUri)
    val verifier = randomUrlSafeString(64)
    val state = randomUrlSafeString(32)
    val authorizationUri = buildAuthorizationUri(
        clientId = clientId,
        redirectUri = redirectUri,
        state = state,
        challenge = verifier,
    )

    callback.start()
    try {
        println("Opening the MyAnimeList authorization page...")
        println("If it does not open automatically, visit:")
        println(authorizationUri)
        openBrowser(authorizationUri)

        val authorizationCode = callback.awaitCode(state)
        val token = exchangeCode(
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUri = redirectUri,
            code = authorizationCode,
            verifier = verifier,
        )
        printConfiguration(token)
    } finally {
        callback.stop()
    }
}

private fun buildAuthorizationUri(
    clientId: String,
    redirectUri: String,
    state: String,
    challenge: String,
): String {
    val parameters = linkedMapOf(
        "response_type" to "code",
        "client_id" to clientId,
        "redirect_uri" to redirectUri,
        "state" to state,
        "code_challenge" to challenge,
        "code_challenge_method" to "plain",
    )
    return "$AUTHORIZATION_URL?${parameters.entries.joinToString("&") { (key, value) ->
        "${encode(key)}=${encode(value)}"
    }}"
}

private fun exchangeCode(
    clientId: String,
    clientSecret: String?,
    redirectUri: String,
    code: String,
    verifier: String,
): TokenResult {
    val parameters = linkedMapOf(
        "client_id" to clientId,
        "grant_type" to "authorization_code",
        "code" to code,
        "code_verifier" to verifier,
        "redirect_uri" to redirectUri,
    ).toMutableMap()
    clientSecret?.let { parameters["client_secret"] = it }
    val request = HttpRequest.newBuilder(URI(TOKEN_URL))
        .header("Content-Type", "application/x-www-form-urlencoded")
        .POST(HttpRequest.BodyPublishers.ofString(formEncode(parameters)))
        .build()
    val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())
    if (response.statusCode() !in 200..299) {
        error("MyAnimeList token exchange failed with HTTP ${response.statusCode()}.")
    }

    val json = Json.parseToJsonElement(response.body()).jsonObject
    return TokenResult(
        accessToken = json.required("access_token"),
        refreshToken = json.required("refresh_token"),
        expiresIn = json.optional("expires_in")?.toLongOrNull(),
    )
}

private fun printConfiguration(token: TokenResult) {
    val rotatedAt = Instant.now()
    val refreshExpiresAt = ZonedDateTime.ofInstant(rotatedAt, ZoneOffset.UTC)
        .plusMonths(1)
        .toInstant()
    println()
    println("Token exchange completed. Copy these values into your local secret store or CI secrets:")
    println("TEST_MAL_ACCESS_TOKEN=${token.accessToken}")
    println("TEST_MAL_REFRESH_TOKEN=${token.refreshToken}")
    token.expiresIn?.let {
        println("TEST_MAL_ACCESS_TOKEN_EXPIRES_AT=${rotatedAt.plus(it, ChronoUnit.SECONDS)}")
    }
    println("TEST_MAL_REFRESH_TOKEN_EXPIRES_AT=$refreshExpiresAt")
    println("Refresh-token expiry is generated as one calendar month after this rotation.")
    println()
    println("The tokens were only printed to this terminal and were not written to disk.")
}

private class CallbackServer(private val redirectUri: String) {
    private val result = CompletableFuture<CallbackResult>()
    private val server: HttpServer

    init {
        val uri = URI(redirectUri)
        server = HttpServer.create(InetSocketAddress(uri.host, uri.port), 0)
        server.createContext(uri.path.ifEmpty { CALLBACK_PATH }) { exchange ->
            handle(exchange)
        }
    }

    fun start() {
        server.start()
    }

    fun stop() {
        server.stop(0)
    }

    fun awaitCode(expectedState: String): String {
        val callback = try {
            result.get(TOKEN_WAIT_SECONDS, TimeUnit.SECONDS)
        } catch (_: TimeoutException) {
            error("Timed out waiting for the OAuth callback.")
        }
        if (callback.state != expectedState) {
            error("OAuth state validation failed.")
        }
        callback.error?.let { error("MyAnimeList authorization failed: $it") }
        return callback.code ?: error("OAuth callback did not contain an authorization code.")
    }

    private fun handle(exchange: HttpExchange) {
        val query = exchange.requestURI.rawQuery.orEmpty()
            .split("&")
            .filter { it.isNotEmpty() }
            .mapNotNull { pair ->
                val parts = pair.split("=", limit = 2)
                if (parts.size == 2) decode(parts[0]) to decode(parts[1]) else null
            }
            .toMap()
        val response = "Authorization received. You can close this browser tab."
        exchange.sendResponseHeaders(200, response.toByteArray().size.toLong())
        exchange.responseBody.use { it.write(response.toByteArray()) }
        result.complete(
            CallbackResult(
                state = query["state"],
                code = query["code"],
                error = query["error"],
            )
        )
    }
}

private data class CallbackResult(
    val state: String?,
    val code: String?,
    val error: String?,
)

private data class TokenResult(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long?,
)

private data class RotationOptions(
    val port: Int = DEFAULT_PORT,
    val redirectUri: String? = null,
    val clientId: String? = null,
    val clientSecret: String? = null,
) {
    companion object {
        fun parse(args: Array<String>): RotationOptions {
            var options = RotationOptions()
            var index = 0
            while (index < args.size) {
                val (key, value) = args[index].split("=", limit = 2).let {
                    if (it.size == 2) it[0] to it[1] else it[0] to null
                }
                fun nextValue(): String = value ?: args.getOrNull(++index)
                    ?: error("$key requires a value.")
                options = when (key) {
                    "--port" -> options.copy(port = nextValue().toInt())
                    "--redirect-uri" -> options.copy(redirectUri = nextValue())
                    "--client-id" -> options.copy(clientId = nextValue())
                    "--client-secret" -> options.copy(clientSecret = nextValue())
                    else -> error("Unknown option: $key")
                }
                index++
            }
            return options
        }
    }
}

private fun printHelp() {
    println("Usage: gradlew :integration-tests:rotateTestTokens [options]")
    println("Options:")
    println("  --port <port>             Local callback port (default: $DEFAULT_PORT)")
    println("  --redirect-uri <uri>      Registered localhost redirect URI override")
    println("  --client-id <id>          Client ID override")
    println("  --client-secret <secret>  Client secret override")
}

private fun loadProperties(): Properties {
    val properties = Properties()
    val workingDirectory = java.io.File(System.getProperty("user.dir"))
    val file = sequenceOf(
        java.io.File(workingDirectory, "local.properties"),
        java.io.File(workingDirectory.parentFile, "local.properties"),
    ).firstOrNull { it.isFile }
    if (file != null) {
        file.inputStream().use { properties.load(it) }
    }
    return properties
}

private fun propertyValue(name: String, properties: Properties): String? =
    System.getenv(name)?.takeIf { it.isNotBlank() }
        ?: properties.getProperty(name)?.takeIf { it.isNotBlank() }

private fun randomUrlSafeString(byteLength: Int): String {
    val bytes = ByteArray(byteLength)
    SecureRandom().nextBytes(bytes)
    return base64Url(bytes)
}

private fun base64Url(value: ByteArray): String =
    Base64.getUrlEncoder().withoutPadding().encodeToString(value)

private fun encode(value: String): String =
    URLEncoder.encode(value, StandardCharsets.UTF_8)

private fun decode(value: String): String =
    URLDecoder.decode(value, StandardCharsets.UTF_8)

private fun formEncode(values: Map<String, String>): String =
    values.entries.joinToString("&") { (key, value) -> "${encode(key)}=${encode(value)}" }

private fun openBrowser(url: String) {
    if (!Desktop.isDesktopSupported()) return
    runCatching { Desktop.getDesktop().browse(URI(url)) }
}

private fun kotlinx.serialization.json.JsonObject.required(name: String): String =
    this[name]?.jsonPrimitive?.content ?: error("Token response did not contain $name.")

private fun kotlinx.serialization.json.JsonObject.optional(name: String): String? =
    this[name]?.jsonPrimitive?.content
