package com.tom.sportevents.core.model

import com.tom.sportevents.core.common.time.DateParser
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

fun NetworkEventItem.toDomain(dateParser: DateParser): EventItem =
    EventItem(
        date = dateParser.parseFromIsoInstantFormat(date),
        id = id,
        imageUrl = imageUrl,
        subtitle = subtitle,
        title = title,
        videoUrl = videoUrl
    )

fun List<NetworkEventItem>.toDomain(dateParser: DateParser): List<EventItem> =
    map { it.toDomain(dateParser) }
