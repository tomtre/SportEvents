package com.tom.sportevents.core.data.mapper

import com.tom.sportevents.core.model.EventItem
import com.tom.sportevents.core.model.ScheduleItem
import com.tom.sportevents.core.network.model.NetworkEventItem
import com.tom.sportevents.core.network.model.NetworkScheduleItem

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

fun NetworkScheduleItem.toDomain(): ScheduleItem =
    ScheduleItem(
        date = date,
        id = id,
        imageUrl = imageUrl,
        subtitle = subtitle,
        title = title
    )

fun List<NetworkScheduleItem>.toDomain(): List<ScheduleItem> =
    map { it.toDomain() }
