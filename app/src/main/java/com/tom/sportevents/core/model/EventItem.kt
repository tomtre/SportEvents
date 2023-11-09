package com.tom.sportevents.core.model

import com.tom.sportevents.core.common.time.TimeManager
import com.tom.sportevents.core.network.model.NetworkEventItem
import java.time.Instant

class EventItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val date: Instant,
    val imageUrl: String,
    val videoUrl: String
)

fun NetworkEventItem.toDomain(timeManager: TimeManager): EventItem =
    EventItem(
        date = timeManager.parseFromIsoInstantFormat(date),
        id = id,
        imageUrl = imageUrl,
        subtitle = subtitle,
        title = title,
        videoUrl = videoUrl
    )

fun List<NetworkEventItem>.toDomain(timeManager: TimeManager): List<EventItem> =
    map { it.toDomain(timeManager) }
