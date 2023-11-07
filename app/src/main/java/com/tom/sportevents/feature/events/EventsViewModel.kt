package com.tom.sportevents.feature.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tom.sportevents.R
import com.tom.sportevents.core.domain.ObserveEventsUseCase
import com.tom.sportevents.core.model.Result
import com.tomczyn.coroutines.Launched
import com.tomczyn.coroutines.stateInMerge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val observeEventsUseCase: ObserveEventsUseCase) : ViewModel() {

    private val refreshEvents = MutableStateFlow(Unit)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _state: MutableStateFlow<EventsState> = MutableStateFlow(EventsState())
        .stateInMerge(
            scope = viewModelScope,
            launched = Launched.WhileSubscribed(5000),
            {
                refreshEvents.flatMapLatest {
                    observeEventsUseCase()
                        .onEachToState { state, result ->
                            Timber.d("XXX", "result: $result")
                            when (result) {
                                is Result.Success -> state.copy(isLoading = false, error = null, items = result.data)
                                is Result.Error -> state.copy(isLoading = false, error = R.string.error_network, items = emptyList())
                            }
                        }
                }
            }
        )

    val state = _state.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            refreshEvents.emit(Unit)
        }
    }

    fun clearNavigation() {
        _state.update { it.copy(navigateToPlayback = null) }
    }

    fun onEventItemClicked(eventUrl: String) {
        _state.update { it.copy(navigateToPlayback = eventUrl) }
    }
}
