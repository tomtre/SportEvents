package com.tom.sportevents.feature.events

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tom.sportevents.feature.common.ui.PullRefreshLazyList

@Composable
internal fun EventsRoute(
    onNavigateToPlayback: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigateToPlayback) {
        uiState.navigateToPlayback?.let {
            onNavigateToPlayback(it)
            viewModel.clearNavigation()
        }
    }

    EventsScreen(
        uiState = uiState,
        onListItemClick = viewModel::onEventItemClicked,
        onRefresh = viewModel::refresh,
        modifier = modifier
    )
}

@Composable
private fun EventsScreen(
    uiState: EventsState,
    onListItemClick: (String) -> Unit,
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
            EventListItem(eventItem = eventItem, onItemClick = onListItemClick)
            Divider(color = Color.Green)
        }
    }
}
