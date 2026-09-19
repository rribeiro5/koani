package io.github.rribeiro5.koani.sample.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import io.github.rribeiro5.koani.sample.compose.detail.anime.AnimeDetailScreen
import io.github.rribeiro5.koani.sample.compose.detail.manga.MangaDetailScreen
import io.github.rribeiro5.koani.sample.compose.ranking.MainListScreen

@Composable
fun App() {
    MaterialTheme {
        val backStack = remember { mutableListOf<Route>(Route.List).toMutableStateList() }
        
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            )
        ) { route ->
            when (route) {
                is Route.List -> NavEntry(route) {
                    MainListScreen(
                        onAnimeClick = { id -> backStack.add(Route.AnimeDetail(id)) },
                        onMangaClick = { id -> backStack.add(Route.MangaDetail(id)) }
                    )
                }
                is Route.AnimeDetail -> NavEntry(route) {
                    AnimeDetailScreen(
                        id = route.id,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
                is Route.MangaDetail -> NavEntry(route) {
                    MangaDetailScreen(
                        id = route.id,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }
        }
    }
}
