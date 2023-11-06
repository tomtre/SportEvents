package com.tom.sportevents.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkScheduleItem(
    @SerialName("date")
    val date: String,
    @SerialName("id")
    val id: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("subtitle")
    val subtitle: String,
    @SerialName("title")
    val title: String
)
