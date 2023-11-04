package com.tom.sportevents.navigation.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tom.sportevents.R
import com.tom.sportevents.feature.events.navigation.eventsNavigationRoute
import com.tom.sportevents.feature.schedule.navigation.scheduleNavigationRoute

enum class HomeTopLevelDestination(
    val route: String,
    @DrawableRes val iconResourceId: Int,
    @StringRes val labelResourceId: Int
) {
    EVENTS(eventsNavigationRoute, R.drawable.ic_events, R.string.events),
    SCHEDULE(scheduleNavigationRoute, R.drawable.ic_schedule, R.string.schedule)
}
