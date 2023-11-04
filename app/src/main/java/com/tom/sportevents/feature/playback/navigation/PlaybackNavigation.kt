@file:Suppress("TopLevelPropertyNaming")

package com.tom.sportevents.feature.playback.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.tom.sportevents.feature.playback.PlaybackRoute

const val playbackNavigationRoute = "playback_route"

fun NavController.navigateToPlayback(streamId: Int, navOptions: NavOptions? = null) {
    // TODO handle streamId
    this.navigate(playbackNavigationRoute, navOptions)
}

fun NavGraphBuilder.playbackScreen() {
    composable(route = playbackNavigationRoute) {
        PlaybackRoute()
    }
}
