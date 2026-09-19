package io.github.rribeiro5.koani.sample.compose.detail.manga

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.rribeiro5.koani.sample.compose.core.ScreenState
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen(id: Int, onBack: () -> Unit) {
    val viewModel: MangaDetailViewModel = koinViewModel(parameters = { parametersOf(id) })
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manga Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (val s = state) {
            is ScreenState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            is ScreenState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Error: ${s.message}") }
            is ScreenState.Success -> {
                val manga = s.data
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = manga.mainPicture?.large ?: manga.mainPicture?.medium,
                        contentDescription = manga.title,
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        contentScale = ContentScale.Fit
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(manga.title, style = MaterialTheme.typography.headlineMedium)
                        Text("Score: ${manga.mean ?: "N/A"}", style = MaterialTheme.typography.titleLarge)
                        Text("Status: ${manga.status ?: "N/A"}")
                        Text("Volumes: ${manga.numVolumes ?: "N/A"}")
                        Text("Chapters: ${manga.numChapters ?: "N/A"}")
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Synopsis", style = MaterialTheme.typography.titleMedium)
                        Text(manga.synopsis ?: "No synopsis available.")
                    }
                }
            }
        }
    }
}
