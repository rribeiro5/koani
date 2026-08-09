package io.github.rribeiro5.koani

import io.github.rribeiro5.koani.anime.AnimeField
import io.github.rribeiro5.koani.anime.AnimeRankingType
import io.github.rribeiro5.koani.anime.Season
import io.github.rribeiro5.koani.anime.SeasonalAnimeSort
import io.github.rribeiro5.koani.anime.UserAnimeListItem
import io.github.rribeiro5.koani.anime.UserAnimeListSortOption
import io.github.rribeiro5.koani.anime.UserAnimeListStatus
import io.github.rribeiro5.koani.anime.UserAnimeListStatusType
import io.github.rribeiro5.koani.anime.mapper.toDomain
import io.github.rribeiro5.koani.auth.MemoryTokenManager
import io.github.rribeiro5.koani.auth.Session
import io.github.rribeiro5.koani.auth.TokenManager
import io.github.rribeiro5.koani.auth.mapper.toSession
import io.github.rribeiro5.koani.core.PaginatedList
import io.github.rribeiro5.koani.core.mapper.toPaginatedList
import io.github.rribeiro5.koani.di.KoaniContainer
import io.github.rribeiro5.koani.manga.MangaField
import io.github.rribeiro5.koani.manga.MangaRankingType
import io.github.rribeiro5.koani.manga.UserMangaListItem
import io.github.rribeiro5.koani.manga.UserMangaListSortOption
import io.github.rribeiro5.koani.manga.UserMangaListStatus
import io.github.rribeiro5.koani.manga.UserMangaListStatusType
import io.github.rribeiro5.koani.manga.mapper.toDomain
import io.github.rribeiro5.koani.util.sanitize
import io.github.rribeiro5.koani.anime.Anime as AnimeModel
import io.github.rribeiro5.koani.anime.RankedAnime as RankedAnimeModel
import io.github.rribeiro5.koani.anime.mapper.toApiValue as toAnimeApiValue
import io.github.rribeiro5.koani.manga.Manga as MangaModel
import io.github.rribeiro5.koani.manga.RankedManga as RankedMangaModel
import io.github.rribeiro5.koani.manga.mapper.toApiValue as toMangaApiValue

public class KoaniClient internal constructor(private val container: KoaniContainer) {

    public val auth: Auth by lazy { Auth(container) }
    public val anime: Anime by lazy { Anime(container) }
    public val manga: Manga by lazy { Manga(container) }

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

        public suspend fun getAnimeRanking(
            rankingType: AnimeRankingType,
            limit: Int? = null,
            offset: Int? = null,
            fields: List<AnimeField>? = null,
        ): PaginatedList<RankedAnimeModel> {
            container.logger.d { "Getting anime ranking for type: ${rankingType.value}" }
            return container.animeService.getAnimeRanking(
                rankingType = rankingType.value,
                limit = limit,
                offset = offset,
                fields = fields?.map { it.fieldName },
            ).toPaginatedList { it.toDomain() }
        }

        public suspend fun getSeasonalAnimes(
            year: Int,
            season: Season,
            sort: SeasonalAnimeSort? = null,
            limit: Int? = null,
            offset: Int? = null,
            fields: List<AnimeField>? = null,
        ): PaginatedList<AnimeModel> {
            container.logger.d { "Getting seasonal anime for $year ${season.value}" }
            return container.animeService.getSeasonalAnimes(
                year = year,
                season = season.value,
                sort = sort?.value,
                limit = limit,
                offset = offset,
                fields = fields?.map { it.fieldName },
            ).toPaginatedList { it.toDomain() }
        }

        public suspend fun getSuggestedAnimes(
            limit: Int? = null,
            offset: Int? = null,
            fields: List<AnimeField>? = null,
        ): PaginatedList<AnimeModel> {
            container.logger.d { "Getting suggested anime" }
            return container.animeService.getSuggestedAnimes(
                limit = limit,
                offset = offset,
                fields = fields?.map { it.fieldName },
            ).toPaginatedList { it.toDomain() }
        }

        public suspend fun getUserAnimeList(
            userName: String = "@me",
            status: UserAnimeListStatusType? = null,
            sortOption: UserAnimeListSortOption? = null,
            limit: Int? = null,
            offset: Int? = null,
        ): PaginatedList<UserAnimeListItem> {
            container.logger.d { "Getting user anime list for user: $userName" }
            return container.animeService.getUserAnimeList(
                userName = userName,
                status = status?.takeIf { it != UserAnimeListStatusType.Unknown }?.toAnimeApiValue(),
                sort = sortOption?.value,
                limit = limit,
                offset = offset,
            ).toPaginatedList { it.toDomain() }
        }

        public suspend fun updateUserAnimeListStatus(
            animeId: Int,
            status: UserAnimeListStatusType? = null,
            isRewatching: Boolean? = null,
            score: Int? = null,
            numWatchedEpisodes: Int? = null,
            priority: Int? = null,
            numTimesRewatched: Int? = null,
            rewatchValue: Int? = null,
            tags: List<String>? = null,
            comments: String? = null,
        ): UserAnimeListStatus {
            container.logger.d { "Updating user anime list status for id: $animeId" }
            return container.animeService.updateUserAnimeListStatus(
                animeId = animeId,
                status = status?.takeIf { it != UserAnimeListStatusType.Unknown }?.toAnimeApiValue(),
                isRewatching = isRewatching,
                score = score,
                numEpisodesWatched = numWatchedEpisodes,
                priority = priority,
                numTimesRewatched = numTimesRewatched,
                rewatchValue = rewatchValue,
                tags = tags,
                comments = comments,
            ).toDomain()
        }

        public suspend fun deleteUserAnimeListItem(
            animeId: Int,
        ) {
            container.logger.d { "Deleting user anime list status for id: $animeId" }
            container.animeService.deleteUserAnimeListItem(animeId = animeId)
        }
    }

    public class Manga internal constructor(private val container: KoaniContainer) {
        public suspend fun getMangaList(
            query: String? = null,
            limit: Int? = null,
            offset: Int? = null,
            fields: List<MangaField>? = null,
        ): PaginatedList<MangaModel> {
            container.logger.d { "Getting manga list${query?.let { " for query: $it" } ?: ""}" }
            return container.mangaService.getMangaList(
                query = query,
                limit = limit,
                offset = offset,
                fields = fields?.map { it.fieldName },
            ).toPaginatedList { it.toDomain() }
        }

        public suspend fun getMangaDetails(
            mangaId: Int,
            fields: List<MangaField>? = null,
        ): MangaModel {
            container.logger.d { "Getting manga details for id: $mangaId" }
            return container.mangaService.getMangaDetails(
                mangaId = mangaId,
                fields = fields?.map { it.fieldName },
            ).toDomain()
        }

        public suspend fun getMangaRanking(
            rankingType: MangaRankingType,
            limit: Int? = null,
            offset: Int? = null,
            fields: List<MangaField>? = null,
        ): PaginatedList<RankedMangaModel> {
            container.logger.d { "Getting manga ranking for type: ${rankingType.value}" }
            return container.mangaService.getMangaRanking(
                rankingType = rankingType.value,
                limit = limit,
                offset = offset,
                fields = fields?.map { it.fieldName },
            ).toPaginatedList { it.toDomain() }
        }

        public suspend fun getUserMangaList(
            userName: String = "@me",
            status: UserMangaListStatusType? = null,
            sortOption: UserMangaListSortOption? = null,
            limit: Int? = null,
            offset: Int? = null,
        ): PaginatedList<UserMangaListItem> {
            container.logger.d { "Getting user manga list for user: $userName" }
            return container.mangaService.getUserMangaList(
                userName = userName,
                status = status?.takeIf { it != UserMangaListStatusType.Unknown }?.toMangaApiValue(),
                sort = sortOption?.value,
                limit = limit,
                offset = offset,
            ).toPaginatedList { it.toDomain() }
        }

        public suspend fun updateUserMangaListStatus(
            mangaId: Int,
            status: UserMangaListStatusType? = null,
            isRereading: Boolean? = null,
            score: Int? = null,
            numVolumesRead: Int? = null,
            numChaptersRead: Int? = null,
            priority: Int? = null,
            numTimesReread: Int? = null,
            rereadValue: Int? = null,
            tags: List<String>? = null,
            comments: String? = null,
        ): UserMangaListStatus {
            container.logger.d { "Updating user manga list status for id: $mangaId" }
            return container.mangaService.updateUserMangaListStatus(
                mangaId = mangaId,
                status = status?.takeIf { it != UserMangaListStatusType.Unknown }?.toMangaApiValue(),
                isRereading = isRereading,
                score = score,
                numVolumesRead = numVolumesRead,
                numChaptersRead = numChaptersRead,
                priority = priority,
                numTimesReread = numTimesReread,
                rereadValue = rereadValue,
                tags = tags,
                comments = comments,
            ).toDomain()
        }

        public suspend fun deleteUserMangaListItem(mangaId: Int) {
            container.logger.d { "Deleting user manga list item for id: $mangaId" }
            container.mangaService.deleteUserMangaListItem(mangaId = mangaId)
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
