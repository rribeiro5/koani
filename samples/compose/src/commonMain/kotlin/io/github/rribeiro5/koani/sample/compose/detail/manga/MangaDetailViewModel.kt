package io.github.rribeiro5.koani.sample.compose.detail.manga

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.rribeiro5.koani.KoaniClient
import io.github.rribeiro5.koani.manga.Manga
import io.github.rribeiro5.koani.manga.MangaField
import io.github.rribeiro5.koani.sample.compose.core.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MangaDetailViewModel(private val client: KoaniClient, private val id: Int) : ViewModel() {
    private val _state = MutableStateFlow<ScreenState<Manga>>(ScreenState.Loading)
    val state: StateFlow<ScreenState<Manga>> = _state.asStateFlow()

    init {
        loadDetails()
    }

    private fun loadDetails() {
        viewModelScope.launch {
            try {
                val manga = client.manga.getMangaDetails(id, fields = MangaField.entries)
                _state.value = ScreenState.Success(manga)
            } catch (e: Exception) {
                _state.value = ScreenState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
