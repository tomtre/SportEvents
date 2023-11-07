package com.tom.sportevents.feature.events

import androidx.annotation.StringRes
import com.tom.sportevents.core.model.EventItem

data class EventsState(
    val isLoading: Boolean = true,
    @StringRes val error: Int? = null,
    val items: List<EventItem> = emptyList(),
    val navigateToPlayback: String? = null
)
