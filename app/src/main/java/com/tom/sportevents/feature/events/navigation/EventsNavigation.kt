@file:Suppress("TopLevelPropertyNaming")

package com.tom.sportevents.feature.events.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.tom.sportevents.feature.events.EventsRoute

const val eventsNavigationRoute = "events_route"

fun NavController.navigateToEvents(navOptions: NavOptions? = null) {
    this.navigate(eventsNavigationRoute, navOptions)
}

fun NavGraphBuilder.eventsScreen(onNavigateToPlayback: (Int) -> Unit) {
    composable(route = eventsNavigationRoute) {
        EventsRoute(onNavigateToPlayback = onNavigateToPlayback)
    }
}
