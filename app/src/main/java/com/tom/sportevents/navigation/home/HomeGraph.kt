@file:Suppress("TopLevelPropertyNaming")

package com.tom.sportevents.navigation.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.tom.sportevents.feature.events.navigation.eventsNavigationRoute
import com.tom.sportevents.feature.events.navigation.navigateToEvents
import com.tom.sportevents.feature.schedule.navigation.navigateToSchedule
import com.tom.sportevents.ui.layout.HomeBottomBarLayout

const val homeNavigationRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeNavigationRoute, navOptions)
}

fun NavGraphBuilder.homeGraph(onNavigateToPlayback: (String) -> Unit) {
    composable(route = homeNavigationRoute) {
        val nestedNavController = rememberNavController()
        val currentDestination: NavDestination? = nestedNavController.currentBackStackEntryAsState().value?.destination
        val allDestinations = HomeTopLevelDestination.values().toList()
        val onNavigateToTopDestination: (HomeTopLevelDestination) -> Unit =
            { topLevelDestination: HomeTopLevelDestination -> navigateToTopLevelDestination(topLevelDestination, nestedNavController) }

        val navHost = remember {
            movableContentOf<PaddingValues> { innerPadding ->
                HomeNavHost(
                    navController = nestedNavController,
                    modifier = Modifier.padding(innerPadding),
                    startDestination = eventsNavigationRoute,
                    onNavigateToPlayback = onNavigateToPlayback
                )
            }
        }

//        Current implementation allows for easily adding support for other layouts depending on the screen size,
//        e.g., ModalNavigationDrawer on expandable phones or PermanentNavigationDrawer on tables/desktops.
        HomeBottomBarLayout(
            destination = allDestinations,
            currentDestination = currentDestination,
            onNavigateToDestination = onNavigateToTopDestination
        ) { innerPadding ->
            navHost(innerPadding)
        }
    }
}

private fun navigateToTopLevelDestination(topLevelDestination: HomeTopLevelDestination, navController: NavController) {
    val topLevelNavOptions = navOptions {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    when (topLevelDestination) {
        HomeTopLevelDestination.EVENTS -> navController.navigateToEvents(topLevelNavOptions)
        HomeTopLevelDestination.SCHEDULE -> navController.navigateToSchedule(topLevelNavOptions)
    }
}
