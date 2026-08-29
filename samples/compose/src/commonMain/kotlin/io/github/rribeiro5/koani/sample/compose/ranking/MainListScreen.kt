package io.github.rribeiro5.koani.sample.compose.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.rribeiro5.koani.anime.Anime
import io.github.rribeiro5.koani.manga.Manga
import io.github.rribeiro5.koani.sample.compose.core.ScreenState
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainListScreen(
    onAnimeClick: (Int) -> Unit,
    onMangaClick: (Int) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Anime", "Manga")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Koani Ranked") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            if (selectedTab == 0) {
                AnimeList(onAnimeClick)
            } else {
                MangaList(onMangaClick)
            }
        }
    }
}

@Composable
private fun AnimeList(onAnimeClick: (Int) -> Unit) {
    val viewModel: AnimeRankingViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    when (val s = state) {
        is ScreenState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        is ScreenState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Error: ${s.message}") }
        is ScreenState.Success -> {
            LazyColumn {
                itemsIndexed(s.data) { index, anime ->
                    RankedItem(
                        rank = index + 1,
                        title = anime.title,
                        imageUrl = anime.mainPicture?.medium,
                        year = anime.startSeason?.year?.toString(),
                        season = anime.startSeason?.season?.name,
                        score = anime.mean,
                        onClick = { onAnimeClick(anime.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MangaList(onMangaClick: (Int) -> Unit) {
    val viewModel: MangaRankingViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    when (val s = state) {
        is ScreenState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        is ScreenState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Error: ${s.message}") }
        is ScreenState.Success -> {
            LazyColumn {
                itemsIndexed(s.data) { index, manga ->
                    RankedItem(
                        rank = index + 1,
                        title = manga.title,
                        imageUrl = manga.mainPicture?.medium,
                        year = manga.startDate?.year?.toString(),
                        score = manga.mean,
                        onClick = { onMangaClick(manga.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RankedItem(
    rank: Int,
    title: String,
    imageUrl: String?,
    year: String?,
    season: String? = null,
    score: Float?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = "#$rank",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Text(
                    text = listOfNotNull(year, season).joinToString(" "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = score?.toString() ?: "N/A",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Black
            )
        }
    }
}
