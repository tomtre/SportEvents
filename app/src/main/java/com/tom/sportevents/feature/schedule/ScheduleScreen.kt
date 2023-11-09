package com.tom.sportevents.feature.schedule

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tom.sportevents.feature.common.ui.PullRefreshLazyList

@Composable
internal fun ScheduleRoute(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    ScheduleScreen(
        uiState = uiState,
        onRefresh = viewModel::refresh,
        modifier = modifier
    )
}

@Composable
private fun ScheduleScreen(
    uiState: ScheduleState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    PullRefreshLazyList(
        isLoading = uiState.isLoading,
        error = uiState.error,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        items(uiState.items, key = { it.id }) { eventItem ->
            ScheduleListItem(scheduleItem = eventItem)
            Divider()
        }
    }
}
