package com.tom.sportevents.core.model

import com.tom.sportevents.core.network.model.NetworkEventItem

class EventItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val date: String,
    val imageUrl: String,
    val videoUrl: String
)

// TODO add date mappings
fun NetworkEventItem.toDomain(): EventItem =
    EventItem(
        date = date,
        id = id,
        imageUrl = imageUrl,
        subtitle = subtitle,
        title = title,
        videoUrl = videoUrl
    )

fun List<NetworkEventItem>.toDomain(): List<EventItem> =
    map { it.toDomain() }
