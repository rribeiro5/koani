package io.github.rribeiro5.koani.sample.compose.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.rribeiro5.koani.KoaniClient
import io.github.rribeiro5.koani.manga.Manga
import io.github.rribeiro5.koani.manga.MangaField
import io.github.rribeiro5.koani.manga.MangaRankingType
import io.github.rribeiro5.koani.sample.compose.core.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MangaRankingViewModel(private val client: KoaniClient) : ViewModel() {
    private val _state = MutableStateFlow<ScreenState<List<Manga>>>(ScreenState.Loading)
    val state: StateFlow<ScreenState<List<Manga>>> = _state.asStateFlow()

    init {
        loadRankedManga()
    }

    fun loadRankedManga() {
        viewModelScope.launch {
            _state.value = ScreenState.Loading
            try {
                val result = client.manga.getMangaRanking(
                    rankingType = MangaRankingType.All,
                    limit = 20,
                    fields = listOf(
                        MangaField.ID,
                        MangaField.TITLE,
                        MangaField.MAIN_PICTURE,
                        MangaField.MEAN,
                        MangaField.START_DATE
                    )
                )
                _state.value = ScreenState.Success(result.data.map { it.manga })
            } catch (e: Exception) {
                _state.value = ScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
