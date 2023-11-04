package com.tom.sportevents.feature.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun EventsRoute(
    onNavigateToPlayback: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // collect state here
    EventsScreen(onNavigateToPlayback, modifier = modifier)
}

@Composable
private fun EventsScreen(
    onNavigateToPlayback: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Events screen")
        Spacer(modifier = Modifier.size(20.dp))
        Button(onClick = { onNavigateToPlayback(0) }) {
            Text(text = "Navigate to Playback screen")
        }
    }
}
