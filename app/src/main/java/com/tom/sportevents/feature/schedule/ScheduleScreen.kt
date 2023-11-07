package com.tom.sportevents.feature.schedule

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
internal fun ScheduleRoute(
    onNavigateToPlayback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // collect state here
    ScheduleScreen(onNavigateToPlayback, modifier = modifier)
}

@Composable
private fun ScheduleScreen(
    onListItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Schedule screen")
        Spacer(modifier = Modifier.size(20.dp))
        Button(onClick = { onListItemClick("") }) {
            Text(text = "Navigate to Playback screen")
        }
    }
}
