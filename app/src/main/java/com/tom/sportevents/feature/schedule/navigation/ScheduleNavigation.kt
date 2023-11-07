@file:Suppress("TopLevelPropertyNaming")

package com.tom.sportevents.feature.schedule.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.tom.sportevents.feature.schedule.ScheduleRoute

const val scheduleNavigationRoute = "schedule_route"

fun NavController.navigateToSchedule(navOptions: NavOptions? = null) {
    this.navigate(scheduleNavigationRoute, navOptions)
}

fun NavGraphBuilder.scheduleScreen(onNavigateToPlayback: (String) -> Unit) {
    composable(route = scheduleNavigationRoute) {
        ScheduleRoute(onNavigateToPlayback = onNavigateToPlayback)
    }
}
