package com.tom.sportevents.feature.events

import androidx.annotation.StringRes
import com.tom.sportevents.core.model.FormattedEventItem

data class EventsState(
    val isLoading: Boolean = false,
    @StringRes val error: Int? = null,
    val items: List<FormattedEventItem> = emptyList(),
    val navigateToPlayback: String? = null
)
