package io.github.rribeiro5.koani.sample.compose.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.rribeiro5.koani.KoaniClient
import io.github.rribeiro5.koani.anime.Anime
import io.github.rribeiro5.koani.anime.AnimeField
import io.github.rribeiro5.koani.anime.AnimeRankingType
import io.github.rribeiro5.koani.sample.compose.core.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnimeRankingViewModel(private val client: KoaniClient) : ViewModel() {
    private val _state = MutableStateFlow<ScreenState<List<Anime>>>(ScreenState.Loading)
    val state: StateFlow<ScreenState<List<Anime>>> = _state.asStateFlow()

    init {
        loadRankedAnime()
    }

    fun loadRankedAnime() {
        viewModelScope.launch {
            _state.value = ScreenState.Loading
            try {
                val result = client.anime.getAnimeRanking(
                    rankingType = AnimeRankingType.All,
                    limit = 20,
                    fields = listOf(
                        AnimeField.ID,
                        AnimeField.TITLE,
                        AnimeField.MAIN_PICTURE,
                        AnimeField.MEAN,
                        AnimeField.START_DATE,
                        AnimeField.START_SEASON
                    )
                )
                _state.value = ScreenState.Success(result.data.map { it.anime })
            } catch (e: Exception) {
                _state.value = ScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
