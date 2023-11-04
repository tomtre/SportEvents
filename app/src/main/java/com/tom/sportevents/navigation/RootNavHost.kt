package com.tom.sportevents.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tom.sportevents.feature.playback.navigation.navigateToPlayback
import com.tom.sportevents.feature.playback.navigation.playbackScreen
import com.tom.sportevents.navigation.home.homeGraph
import com.tom.sportevents.navigation.home.homeNavigationRoute

@Composable
fun RootNavHost(
    navController: NavHostController = rememberNavController(),
    starDestination: String = homeNavigationRoute
) {
    NavHost(navController = navController, startDestination = starDestination) {
        homeGraph(onNavigateToPlayback = { streamId -> navController.navigateToPlayback(streamId) })
        playbackScreen()
    }
}
