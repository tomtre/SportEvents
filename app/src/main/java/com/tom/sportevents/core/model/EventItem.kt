package com.tom.sportevents.core.model

import com.tom.sportevents.core.common.time.AtomicTimeManager
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

fun NetworkEventItem.toDomain(timeManager: AtomicTimeManager): EventItem =
    EventItem(
        date = timeManager.parseFromIsoInstantFormat(date),
        id = id,
        imageUrl = imageUrl,
        subtitle = subtitle,
        title = title,
        videoUrl = videoUrl
    )

fun List<NetworkEventItem>.toDomain(atomicTimeManager: AtomicTimeManager): List<EventItem> =
    map { it.toDomain(atomicTimeManager) }
