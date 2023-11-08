package com.tom.sportevents.feature.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tom.sportevents.R
import com.tom.sportevents.core.common.MutableBehaviorFlow
import com.tom.sportevents.core.domain.ObserveScheduleUseCase
import com.tom.sportevents.core.model.Result
import com.tomczyn.coroutines.Launched
import com.tomczyn.coroutines.stateInMerge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(private val observeScheduleUseCase: ObserveScheduleUseCase) : ViewModel() {

    private val refreshSchedule = MutableBehaviorFlow(Unit)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _state: MutableStateFlow<ScheduleState> = MutableStateFlow(ScheduleState(isLoading = true))
        .stateInMerge(
            scope = viewModelScope,
            launched = Launched.WhileSubscribed(5_000),
            {
                refreshSchedule.flatMapLatest {
                    observeScheduleUseCase()
                        .onEachToState { state, result ->
                            when (result) {
                                is Result.Error -> state.copy(isLoading = false, error = R.string.error_network, items = emptyList())
                                is Result.Success -> state.copy(isLoading = false, error = null, items = result.data)
                            }
                        }
                }
            }
        )

    val state = _state.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            refreshSchedule.emit(Unit)
        }
    }
}
