package com.tom.sportevents.core.model

import com.tom.sportevents.core.common.time.AtomicTimeManager
import com.tom.sportevents.core.network.model.NetworkScheduleItem
import java.time.Instant

data class ScheduleItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val date: Instant,
    val imageUrl: String
)

fun NetworkScheduleItem.toDomain(timeManager: AtomicTimeManager): ScheduleItem =
    ScheduleItem(
        date = timeManager.parseFromIsoInstantFormat(date),
        id = id,
        imageUrl = imageUrl,
        subtitle = subtitle,
        title = title
    )

fun List<NetworkScheduleItem>.toDomain(timeManager: AtomicTimeManager): List<ScheduleItem> =
    map { it.toDomain(timeManager) }
