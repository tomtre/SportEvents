package com.tom.sportevents.feature.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tom.sportevents.R

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EventsScreen(
    uiState: EventsState,
    onListItemClick: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = uiState.isLoading, onRefresh = onRefresh)

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.error == null) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(uiState.items, key = { it.id }) { eventItem ->
                    EventListItem(eventItem = eventItem, onItemClick = onListItemClick)
                    Divider(color = Color.Green)
                }
            }
            PullRefreshIndicator(
                refreshing = uiState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = uiState.error))
                Button(onClick = onRefresh, modifier = Modifier.padding(top = 10.dp)) {
                    Text(stringResource(id = R.string.refresh))
                }
            }
        }
    }
}
