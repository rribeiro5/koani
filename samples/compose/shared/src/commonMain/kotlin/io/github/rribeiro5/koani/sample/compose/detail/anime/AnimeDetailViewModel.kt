package io.github.rribeiro5.koani.sample.compose.detail.anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.rribeiro5.koani.KoaniClient
import io.github.rribeiro5.koani.anime.Anime
import io.github.rribeiro5.koani.anime.AnimeField
import io.github.rribeiro5.koani.sample.compose.core.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnimeDetailViewModel(private val client: KoaniClient, private val id: Int) : ViewModel() {
    private val _state = MutableStateFlow<ScreenState<Anime>>(ScreenState.Loading)
    val state: StateFlow<ScreenState<Anime>> = _state.asStateFlow()

    init {
        loadDetails()
    }

    private fun loadDetails() {
        viewModelScope.launch {
            try {
                val anime = client.anime.getAnimeDetails(id, fields = AnimeField.entries)
                _state.value = ScreenState.Success(anime)
            } catch (e: Exception) {
                _state.value = ScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
