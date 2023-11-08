package com.tom.sportevents.feature.playback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView

@Composable
internal fun PlaybackRoute(
    modifier: Modifier = Modifier,
    viewModel: PlaybackViewModel = hiltViewModel()
) {
    PlaybackScreen(player = viewModel.player, modifier = modifier)
}

@Composable
private fun PlaybackScreen(
    // I found this solution on the Internet but the Player is probably an unstable parameter,
    // it breaks the functionality of skipping recomposition
    player: Player,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var lifecycle by remember {
            mutableStateOf(Lifecycle.Event.ON_CREATE)
        }
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                lifecycle = event
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    this.player = player
                }
            },
            update = { playerView ->
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        playerView.onPause()
                        playerView.player?.pause()
                    }

                    Lifecycle.Event.ON_RESUME -> playerView.onResume()
                    else -> Unit
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
        )

    }
}
