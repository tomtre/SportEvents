package com.tom.sportevents.feature.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tom.sportevents.R
import com.tom.sportevents.core.domain.GetEventsUseCase
import com.tom.sportevents.core.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val getEventsUseCase: GetEventsUseCase) : ViewModel() {

    private val _state: MutableStateFlow<EventsState> = MutableStateFlow(EventsState())

    init {
        viewModelScope.launch {
            loadData()
        }
    }

    val state = _state.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        _state.update { it.copy(isLoading = true, error = null) }
        when (val result = getEventsUseCase()) {
            is Result.Success -> _state.update { it.copy(isLoading = false, error = null, items = result.data) }
            is Result.Error -> _state.update { it.copy(isLoading = false, error = R.string.error_network, items = emptyList()) }
        }
    }

    fun clearNavigation() {
        _state.update { it.copy(navigateToPlayback = null) }
    }

    fun onEventItemClicked(eventUrl: String) {
        _state.update { it.copy(navigateToPlayback = eventUrl) }
    }
}
