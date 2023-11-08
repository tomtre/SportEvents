package com.tom.sportevents.feature.playback

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.tom.sportevents.feature.playback.navigation.PlaybackArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    // I found this solution on the Internet but I don't like injecting Player this way.
    // There's probably a better approach, but I haven't had time to do any research how to do it.
    val player: Player
) : ViewModel() {

    private val playbackArgs = PlaybackArgs(savedStateHandle)

    init {
        player.prepare()
        player.addMediaItem(MediaItem.fromUri(playbackArgs.streamUrl))
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }
}
