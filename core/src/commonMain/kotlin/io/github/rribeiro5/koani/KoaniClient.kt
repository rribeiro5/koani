package io.github.rribeiro5.koani

import io.github.rribeiro5.koani.anime.AnimeField
import io.github.rribeiro5.koani.anime.mapper.toDomain
import io.github.rribeiro5.koani.auth.MemoryTokenManager
import io.github.rribeiro5.koani.auth.Session
import io.github.rribeiro5.koani.auth.TokenManager
import io.github.rribeiro5.koani.auth.mapper.toSession
import io.github.rribeiro5.koani.core.PaginatedList
import io.github.rribeiro5.koani.core.mapper.toPaginatedList
import io.github.rribeiro5.koani.di.KoaniContainer
import io.github.rribeiro5.koani.util.sanitize
import io.github.rribeiro5.koani.anime.Anime as AnimeModel

public class KoaniClient internal constructor(private val container: KoaniContainer) {

    public val auth: Auth by lazy { Auth(container) }
    public val anime: Anime by lazy { Anime(container) }

    init {
        require(container.clientId.isNotBlank()) {
            "Client ID cannot be empty".also { container.logger.e(it) }
        }
        container.logger.d { "KoaniClient successfully initialized" }
    }

    public class Auth internal constructor(private val container: KoaniContainer) {
        public val clientId: String
            get() = container.clientId
        public val clientSecret: String?
            get() = container.clientSecret
        public val tokenManager: TokenManager
            get() = container.tokenManager

        public suspend fun authenticate(
            authorizationCode: String,
            codeVerifier: String,
            redirectUri: String? = null,
        ): Session {
            container.logger.d { "Authenticating user..." }
            container.logger.v { "Authenticating with authorizationCode=${authorizationCode.sanitize()}" }
            val response = container.authService.authenticate(
                clientId = clientId,
                authorizationCode = authorizationCode,
                codeVerifier = codeVerifier,
                clientSecret = clientSecret,
                redirectUri = redirectUri,
            )
            tokenManager.storeTokens(response.accessToken, response.refreshToken)
            container.logger.i { "Successfully authenticated user" }
            return response.toSession()
        }

        public suspend fun refreshTokens(refreshToken: String? = tokenManager.refreshToken()): Session {
            container.logger.d { "Refreshing tokens..." }
            val token = refreshToken ?: run {
                container.logger.e { "Refresh tokens failed: No refresh token available" }
                throw IllegalStateException("No refresh token available")
            }

            val response = container.authService.refreshTokens(
                clientId = clientId,
                refreshToken = token,
                clientSecret = clientSecret,
            )
            tokenManager.storeTokens(response.accessToken, response.refreshToken)
            container.logger.i { "Successfully refreshed tokens" }
            return response.toSession()
        }

        public fun logout() {
            container.logger.d { "Logging out user..." }
            tokenManager.clearTokens()
            container.logger.i { "User logged out successfully" }
        }
    }

    public class Anime internal constructor(private val container: KoaniContainer) {
        public suspend fun getAnimeList(
            query: String? = null,
            limit: Int? = null,
            offset: Int? = null,
            fields: List<AnimeField>? = null,
        ): PaginatedList<AnimeModel> {
            container.logger.d { "Getting anime list${query?.let { " for query: $it" } ?: ""}" }
            return container.animeService.getAnimeList(
                query = query,
                limit = limit,
                offset = offset,
                fields = fields?.map { it.fieldName },
            ).toPaginatedList { it.toDomain() }
        }

        public suspend fun getAnimeDetails(
            animeId: Int,
            fields: List<AnimeField>? = null,
        ): AnimeModel {
            container.logger.d { "Getting anime details for id: $animeId" }
            return container.animeService.getAnimeDetails(
                animeId = animeId,
                fields = fields?.map { it.fieldName },
            ).toDomain()
        }
    }

    public class Builder(internal val clientId: String) {
        internal var clientSecret: String? = null
        internal var timeoutMillis: Long? = null
        internal var logLevel: LogLevel = LogLevel.NONE
        internal var tokenManager: TokenManager = MemoryTokenManager()

        public fun clientSecret(clientSecret: String?): Builder = apply {
            this.clientSecret = clientSecret
        }

        public fun timeoutMillis(timeoutMillis: Long): Builder = apply {
            this.timeoutMillis = timeoutMillis
        }

        public fun logLevel(logLevel: LogLevel): Builder = apply {
            this.logLevel = logLevel
        }

        public fun tokenManager(tokenManager: TokenManager): Builder = apply {
            this.tokenManager = tokenManager
        }

        public fun build(): KoaniClient {
            val container = KoaniContainer(
                clientId = clientId,
                clientSecret = clientSecret,
                tokenManager = tokenManager,
                timeoutMillis = timeoutMillis,
                logLevel = logLevel,
            )

            container.logger.d { "Initializing KoaniClient" }
            container.logger.v {
                val sanitizedClientId = clientId.sanitize()
                val sanitizedClientSecret = clientSecret?.sanitize()
                "Initializing KoaniClient (clientId=$sanitizedClientId, clientSecret=$sanitizedClientSecret, timeoutMillis=$timeoutMillis, logLevel=$logLevel)"
            }

            return KoaniClient(container)
        }
    }
}
