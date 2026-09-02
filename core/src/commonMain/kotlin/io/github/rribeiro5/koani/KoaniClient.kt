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
import io.github.rribeiro5.koani.forum.ForumCategory
import io.github.rribeiro5.koani.forum.ForumTopic
import io.github.rribeiro5.koani.forum.ForumTopicDetail
import io.github.rribeiro5.koani.forum.mapper.toDomain
import io.github.rribeiro5.koani.manga.MangaField
import io.github.rribeiro5.koani.manga.MangaRankingType
import io.github.rribeiro5.koani.manga.UserMangaListItem
import io.github.rribeiro5.koani.manga.UserMangaListSortOption
import io.github.rribeiro5.koani.manga.UserMangaListStatus
import io.github.rribeiro5.koani.manga.UserMangaListStatusType
import io.github.rribeiro5.koani.manga.mapper.toDomain
import io.github.rribeiro5.koani.user.UserField
import io.github.rribeiro5.koani.user.mapper.toDomain
import io.github.rribeiro5.koani.util.sanitize
import io.github.rribeiro5.koani.anime.Anime as AnimeModel
import io.github.rribeiro5.koani.anime.RankedAnime as RankedAnimeModel
import io.github.rribeiro5.koani.anime.mapper.toApiValue as toAnimeApiValue
import io.github.rribeiro5.koani.user.User as UserModel
import io.github.rribeiro5.koani.manga.Manga as MangaModel
import io.github.rribeiro5.koani.manga.RankedManga as RankedMangaModel
import io.github.rribeiro5.koani.manga.mapper.toApiValue as toMangaApiValue

/**
 * Main entry point for the Koani library to interact with the MyAnimeList API.
 *
 * Use [KoaniClient.Builder] to create an instance of this client.
 *
 * @property container The internal container holding dependencies and configuration.
 */
public class KoaniClient internal constructor(private val container: KoaniContainer) {

    /**
     * Entry point for authentication-related operations.
     */
    public val auth: Auth by lazy { Auth(container) }

    /**
     * Entry point for anime-related operations.
     */
    public val anime: Anime by lazy { Anime(container) }

    /**
     * Entry point for manga-related operations.
     */
    public val manga: Manga by lazy { Manga(container) }

    /**
     * Entry point for forum-related operations.
     */
    public val forum: Forum by lazy { Forum(container) }

    /**
     * Entry point for user-related operations.
     */
    public val user: User by lazy { User(container) }

    init {
        require(container.clientId.isNotBlank()) {
            "Client ID cannot be empty".also { container.logger.e(it) }
        }
        container.logger.d { "KoaniClient successfully initialized" }
    }

    /**
     * Handles authentication and token management.
     *
     * @property container The internal container holding dependencies and configuration.
     */
    public class Auth internal constructor(private val container: KoaniContainer) {
        /**
         * The MyAnimeList Client ID.
         */
        public val clientId: String
            get() = container.clientId

        /**
         * The MyAnimeList Client Secret, if applicable.
         */
        public val clientSecret: String?
            get() = container.clientSecret

        /**
         * The token manager used to store and retrieve authentication tokens.
         */
        public val tokenManager: TokenManager
            get() = container.tokenManager

        /**
         * Authenticates the user using an authorization code.
         *
         * @param authorizationCode The authorization code obtained from MyAnimeList.
         * @param codeVerifier The PKCE code verifier.
         * @param redirectUri The redirect URI used during the authorization request.
         * @return The [Session] containing the new tokens.
         * @see <a href="https://myanimelist.net/apiconfig/references/authorization#step-6-exchange-authorization-code-for-refresh-and-access-tokens">Token Exchange Endpoint</a>
         */
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

        /**
         * Refreshes the authentication tokens.
         *
         * @param refreshToken The refresh token to use. If null, the one from [tokenManager] is used.
         * @return The [Session] containing the new tokens.
         * @throws IllegalStateException if no refresh token is available.
         * @see <a href="https://myanimelist.net/apiconfig/references/authorization#refreshing-an-access-token">Token Refresh Endpoint</a>
         */
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

        /**
         * Logs out the user by clearing the stored tokens.
         */
        public fun logout() {
            container.logger.d { "Logging out user..." }
            tokenManager.clearTokens()
            container.logger.i { "User logged out successfully" }
        }
    }

    /**
     * Handles anime-related operations.
     *
     * @property container The internal container holding dependencies and configuration.
     */
    public class Anime internal constructor(private val container: KoaniContainer) {
        /**
         * Searches for anime based on a query.
         *
         * @param query The search query string.
         * @param limit The maximum number of results to return.
         * @param offset The number of results to skip.
         * @param fields The list of optional fields to include in the response.
         * @return A [PaginatedList] of [AnimeModel].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/anime_get">Get anime list</a>
         */
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

        /**
         * Retrieves details for a specific anime.
         *
         * @param animeId The unique identifier of the anime.
         * @param fields The list of optional fields to include in the response.
         * @return The [AnimeModel] details.
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/anime_anime_id_get">Get anime details</a>
         */
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

        /**
         * Retrieves anime ranking.
         *
         * @param rankingType The type of ranking to retrieve.
         * @param limit The maximum number of results to return.
         * @param offset The number of results to skip.
         * @param fields The list of optional fields to include in the response.
         * @return A [PaginatedList] of [RankedAnimeModel].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/anime_ranking_get">Get anime ranking</a>
         */
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

        /**
         * Retrieves seasonal anime.
         *
         * @param year The year of the season.
         * @param season The season.
         * @param sort The sorting option.
         * @param limit The maximum number of results to return.
         * @param offset The number of results to skip.
         * @param fields The list of optional fields to include in the response.
         * @return A [PaginatedList] of [AnimeModel].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/anime_season_year_season_get">Get seasonal anime</a>
         */
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

        /**
         * Retrieves suggested anime for the user.
         *
         * @param limit The maximum number of results to return.
         * @param offset The number of results to skip.
         * @param fields The list of optional fields to include in the response.
         * @return A [PaginatedList] of [AnimeModel].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/anime_suggestions_get">Get suggested anime</a>
         */
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

        /**
         * Retrieves the anime list of a user.
         *
         * @param userName The username. Use "@me" for the authenticated user.
         * @param status The status of the anime list items.
         * @param sortOption The sorting option.
         * @param limit The maximum number of results to return.
         * @param offset The number of results to skip.
         * @return A [PaginatedList] of [UserAnimeListItem].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/users_user_name_animelist_get">Get user anime list</a>
         */
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

        /**
         * Updates or adds an anime to the user's anime list.
         *
         * @param animeId The unique identifier of the anime.
         * @param status The status of the anime.
         * @param isRewatching Whether the user is rewatching the anime.
         * @param score The score given by the user (0-10).
         * @param numWatchedEpisodes The number of watched episodes.
         * @param priority The priority in the list.
         * @param numTimesRewatched The number of times rewatched.
         * @param rewatchValue The rewatch value.
         * @param tags The tags added by the user.
         * @param comments The comments added by the user.
         * @return The updated [UserAnimeListStatus].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/anime_anime_id_my_list_status_put">Update my anime list status</a>
         */
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

        /**
         * Removes an anime from the user's anime list.
         *
         * @param animeId The unique identifier of the anime.
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/anime_anime_id_my_list_status_delete">Delete my anime list item</a>
         */
        public suspend fun deleteUserAnimeListItem(
            animeId: Int,
        ) {
            container.logger.d { "Deleting user anime list status for id: $animeId" }
            container.animeService.deleteUserAnimeListItem(animeId = animeId)
        }
    }

    /**
     * Handles manga-related operations.
     *
     * @property container The internal container holding dependencies and configuration.
     */
    public class Manga internal constructor(private val container: KoaniContainer) {
        /**
         * Searches for manga based on a query.
         *
         * @param query The search query string.
         * @param limit The maximum number of results to return.
         * @param offset The number of results to skip.
         * @param fields The list of optional fields to include in the response.
         * @return A [PaginatedList] of [MangaModel].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/manga_get">Get manga list</a>
         */
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

        /**
         * Retrieves details for a specific manga.
         *
         * @param mangaId The unique identifier of the manga.
         * @param fields The list of optional fields to include in the response.
         * @return The [MangaModel] details.
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/manga_manga_id_get">Get manga details</a>
         */
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

        /**
         * Retrieves manga ranking.
         *
         * @param rankingType The type of ranking to retrieve.
         * @param limit The maximum number of results to return.
         * @param offset The number of results to skip.
         * @param fields The list of optional fields to include in the response.
         * @return A [PaginatedList] of [RankedMangaModel].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/manga_ranking_get">Get manga ranking</a>
         */
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

        /**
         * Retrieves the manga list of a user.
         *
         * @param userName The username. Use "@me" for the authenticated user.
         * @param status The status of the manga list items.
         * @param sortOption The sorting option.
         * @param limit The maximum number of results to return.
         * @param offset The number of results to skip.
         * @return A [PaginatedList] of [UserMangaListItem].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/users_user_name_mangalist_get">Get user manga list</a>
         */
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

        /**
         * Updates or adds a manga to the user's manga list.
         *
         * @param mangaId The unique identifier of the manga.
         * @param status The status of the manga.
         * @param isRereading Whether the user is rereading the manga.
         * @param score The score given by the user (0-10).
         * @param numVolumesRead The number of volumes read.
         * @param numChaptersRead The number of chapters read.
         * @param priority The priority in the list.
         * @param numTimesReread The number of times reread.
         * @param rereadValue The reread value.
         * @param tags The tags added by the user.
         * @param comments The comments added by the user.
         * @return The updated [UserMangaListStatus].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/manga_manga_id_my_list_status_put">Update my manga list status</a>
         */
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

        /**
         * Removes a manga from the user's manga list.
         *
         * @param mangaId The unique identifier of the manga.
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/manga_manga_id_my_list_status_delete">Delete my manga list item</a>
         */
        public suspend fun deleteUserMangaListItem(mangaId: Int) {
            container.logger.d { "Deleting user manga list item for id: $mangaId" }
            container.mangaService.deleteUserMangaListItem(mangaId = mangaId)
        }
    }

    /**
     * Handles forum-related operations.
     *
     * @property container The internal container holding dependencies and configuration.
     */
    public class Forum internal constructor(private val container: KoaniContainer) {
        /**
         * Retrieves the list of forum boards.
         *
         * @return A list of [ForumCategory].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/forum_boards_get">Get forum boards</a>
         */
        public suspend fun getForumBoards(): List<ForumCategory> {
            container.logger.d { "Getting forum boards" }
            return container.forumService.getForumBoards().categories.map { it.toDomain() }
        }

        /**
         * Searches for forum topics.
         *
         * @param boardId Filter by board ID.
         * @param subboardId Filter by subboard ID.
         * @param query Search query string.
         * @param topicUserName Filter by the username of the topic creator.
         * @param userName Filter by the username of a participant.
         * @param limit The maximum number of results to return.
         * @param offset The number of results to skip.
         * @return A [PaginatedList] of [ForumTopic].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/forum_topics_get">Get forum topics</a>
         */
        public suspend fun getForumTopics(
            boardId: Int? = null,
            subboardId: Int? = null,
            query: String? = null,
            topicUserName: String? = null,
            userName: String? = null,
            limit: Int? = null,
            offset: Int? = null,
        ): PaginatedList<ForumTopic> {
            container.logger.d { "Getting forum topics" }
            return container.forumService.getForumTopics(
                boardId = boardId,
                subboardId = subboardId,
                query = query,
                topicUserName = topicUserName,
                userName = userName,
                limit = limit,
                offset = offset,
            ).toPaginatedList { it.toDomain() }
        }

        /**
         * Retrieves details for a specific forum topic, including its posts.
         *
         * @param topicId The unique identifier of the forum topic.
         * @param limit The maximum number of posts to return.
         * @param offset The number of posts to skip.
         * @return The [ForumTopicDetail].
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/forum_topic_id_get">Get forum topic detail</a>
         */
        public suspend fun getForumTopicDetail(
            topicId: Int,
            limit: Int? = null,
            offset: Int? = null,
        ): ForumTopicDetail {
            container.logger.d { "Getting forum topic details for id: $topicId" }
            return container.forumService.getForumTopicDetail(
                topicId = topicId,
                limit = limit,
                offset = offset,
            ).toDomain()
        }
    }

    /**
     * Handles user-related operations.
     *
     * @property container The internal container holding dependencies and configuration.
     */
    public class User internal constructor(private val container: KoaniContainer) {
        /**
         * Retrieves details for a specific user.
         *
         * @param userName The username. Use "@me" for the authenticated user.
         * @param fields The list of optional fields to include in the response.
         * @return The [UserModel] details.
         * @see <a href="https://myanimelist.net/apiconfig/references/api/v2#operation/users_user_name_get">Get my user information</a>
         */
        public suspend fun getUserDetails(
            userName: String = "@me",
            fields: List<UserField>? = null,
        ): UserModel {
            container.logger.d { "Getting user details for user: $userName" }
            return container.userService.getUserDetails(
                userName = userName,
                fields = fields?.map { it.fieldName },
            ).toDomain()
        }
    }

    /**
     * Builder for [KoaniClient].
     *
     * @property clientId The MyAnimeList Client ID.
     */
    public class Builder(internal val clientId: String) {
        internal var clientSecret: String? = null
        internal var timeoutMillis: Long? = null
        internal var logLevel: LogLevel = LogLevel.NONE
        internal var tokenManager: TokenManager = MemoryTokenManager()

        /**
         * Sets the MyAnimeList Client Secret.
         *
         * @param clientSecret The client secret.
         * @return This builder instance.
         */
        public fun clientSecret(clientSecret: String?): Builder = apply {
            this.clientSecret = clientSecret
        }

        /**
         * Sets the timeout for network requests in milliseconds.
         *
         * @param timeoutMillis The timeout in milliseconds.
         * @return This builder instance.
         */
        public fun timeoutMillis(timeoutMillis: Long): Builder = apply {
            this.timeoutMillis = timeoutMillis
        }

        /**
         * Sets the log level for the client.
         *
         * @param logLevel The [LogLevel] to use.
         * @return This builder instance.
         */
        public fun logLevel(logLevel: LogLevel): Builder = apply {
            this.logLevel = logLevel
        }

        /**
         * Sets the token manager for the client.
         *
         * @param tokenManager The [TokenManager] to use.
         * @return This builder instance.
         */
        public fun tokenManager(tokenManager: TokenManager): Builder = apply {
            this.tokenManager = tokenManager
        }

        /**
         * Builds and returns a new [KoaniClient] instance.
         *
         * @return A new [KoaniClient] instance.
         */
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
