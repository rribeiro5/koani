package io.github.rribeiro5.koani.sample.compose.di

import io.github.rribeiro5.koani.KoaniClient
import io.github.rribeiro5.koani.sample.compose.clientId
import io.github.rribeiro5.koani.sample.compose.ranking.AnimeRankingViewModel
import io.github.rribeiro5.koani.sample.compose.ranking.MangaRankingViewModel
import io.github.rribeiro5.koani.sample.compose.detail.anime.AnimeDetailViewModel
import io.github.rribeiro5.koani.sample.compose.detail.manga.MangaDetailViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes
import org.koin.dsl.module

val appModule = module {
    single { KoaniClient.Builder(clientId).build() }
    
    viewModel { AnimeRankingViewModel(get()) }
    viewModel { MangaRankingViewModel(get()) }
    viewModel { (id: Int) -> AnimeDetailViewModel(get(), id) }
    viewModel { (id: Int) -> MangaDetailViewModel(get(), id) }
}

fun initKoin(appDeclaration: KoinAppDeclaration? = null) =
    startKoin {
        includes(appDeclaration)
        modules(appModule)
    }
