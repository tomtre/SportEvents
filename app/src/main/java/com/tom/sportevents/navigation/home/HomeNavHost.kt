package com.tom.sportevents.navigation.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.tom.sportevents.feature.events.navigation.eventsScreen
import com.tom.sportevents.feature.schedule.navigation.scheduleScreen

@Composable
fun HomeNavHost(
    navController: NavHostController,
    onNavigateToPlayback: (String) -> Unit,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        eventsScreen(onNavigateToPlayback = onNavigateToPlayback)
        scheduleScreen()
    }
}
