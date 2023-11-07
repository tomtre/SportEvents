package com.tom.sportevents.core.model

import com.tom.sportevents.core.network.model.NetworkScheduleItem

data class ScheduleItem(
    val date: String,
    val id: String,
    val imageUrl: String,
    val subtitle: String,
    val title: String
)

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
