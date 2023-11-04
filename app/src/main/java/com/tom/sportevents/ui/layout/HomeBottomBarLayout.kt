package com.tom.sportevents.ui.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.tom.sportevents.navigation.home.HomeTopLevelDestination

@Composable
fun HomeBottomBarLayout(
    destination: List<HomeTopLevelDestination>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (HomeTopLevelDestination) -> Unit,
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    Scaffold(bottomBar = {
        BottomBar(
            destinations = destination,
            onNavigateToDestination = onNavigateToDestination,
            currentDestination = currentDestination
        )
    }) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
private fun BottomBar(
    destinations: List<HomeTopLevelDestination>,
    onNavigateToDestination: (HomeTopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    NavigationBar {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(destination.iconResourceId),
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(destination.labelResourceId)) },
                selected = selected,
                onClick = { onNavigateToDestination(destination) }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: HomeTopLevelDestination) =
    this?.hierarchy?.any { it.route == destination.route } ?: false
