package com.tom.sportevents.feature.schedule

import androidx.annotation.StringRes
import com.tom.sportevents.core.model.FormattedScheduleItem

data class ScheduleState(
    val isLoading: Boolean = false,
    @StringRes val error: Int? = null,
    val items: List<FormattedScheduleItem> = emptyList(),
    val navigateToPlayback: String? = null
)
