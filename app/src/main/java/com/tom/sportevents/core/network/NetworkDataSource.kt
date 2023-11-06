package com.tom.sportevents.core.network

import com.tom.sportevents.core.network.model.CustomApiErrorResponse
import com.tom.sportevents.core.network.model.EventsResponse
import com.tom.sportevents.core.network.model.ScheduleResponse
import com.tom.sportevents.core.network.util.Response

interface NetworkDataSource {
    suspend fun getEvents(): Response<CustomApiErrorResponse, EventsResponse>

    suspend fun getSchedule(): Response<CustomApiErrorResponse, ScheduleResponse>
}
