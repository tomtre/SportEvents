@file:Suppress("TopLevelPropertyNaming")

package com.tom.sportevents.feature.playback.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tom.sportevents.feature.playback.PlaybackRoute
import java.net.URLDecoder
import java.net.URLEncoder

const val playbackNavigationRoute = "playback_route"
const val streamUrlArg = "stream_url_arg"

fun NavController.navigateToPlayback(playbackArgs: PlaybackArgs, navOptions: NavOptions? = null) {
    val streamUrlEncoded = URLEncoder.encode(playbackArgs.streamUrl, "utf-8")
    this.navigate("$playbackNavigationRoute/$streamUrlEncoded", navOptions)
}

data class PlaybackArgs(val streamUrl: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        streamUrl = URLDecoder.decode(checkNotNull(savedStateHandle[streamUrlArg]), "utf-8")
    )
}

fun NavGraphBuilder.playbackScreen() {
    composable(
        route = "$playbackNavigationRoute/{$streamUrlArg}",
        arguments = listOf(navArgument(streamUrlArg) { type = NavType.StringType })
    ) {
        PlaybackRoute()
    }
}
