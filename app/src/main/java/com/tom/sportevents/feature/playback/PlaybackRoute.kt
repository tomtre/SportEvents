package com.tom.sportevents.feature.playback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun PlaybackRoute(
    modifier: Modifier = Modifier
) {
    // collect state here
    PlaybackScreen(modifier = modifier)
}

@Composable
private fun PlaybackScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Playback screen")
    }
}
