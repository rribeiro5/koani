package io.github.rribeiro5.koani.user.mapper

import io.github.rribeiro5.koani.core.mapper.toDate
import io.github.rribeiro5.koani.core.mapper.toDateTime
import io.github.rribeiro5.koani.user.User
import io.github.rribeiro5.koani.user.UserAnimeStatistics
import io.github.rribeiro5.koani.user.dto.UserAnimeStatisticsResponse
import io.github.rribeiro5.koani.user.dto.UserResponse

internal fun UserResponse.toDomain(): User = User(
    id = id,
    name = name,
    picture = picture,
    gender = gender,
    birthday = birthday?.toDate(),
    location = location,
    joinedAt = joinedAt?.toDateTime(),
    animeStatistics = animeStatistics?.toDomain(),
    timeZone = timeZone,
    isSupporter = isSupporter,
)

internal fun UserAnimeStatisticsResponse.toDomain(): UserAnimeStatistics = UserAnimeStatistics(
    numItemsWatching = numItemsWatching,
    numItemsCompleted = numItemsCompleted,
    numItemsOnHold = numItemsOnHold,
    numItemsDropped = numItemsDropped,
    numItemsPlanToWatch = numItemsPlanToWatch,
    numItems = numItems,
    numDaysWatched = numDaysWatched,
    numDaysWatching = numDaysWatching,
    numDaysCompleted = numDaysCompleted,
    numDaysOnHold = numDaysOnHold,
    numDaysDropped = numDaysDropped,
    numDays = numDays,
    numEpisodes = numEpisodes,
    numTimesRewatched = numTimesRewatched,
    meanScore = meanScore,
)
