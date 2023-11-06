package com.tom.sportevents.core.network

import com.tom.sportevents.core.network.di.ApiUrl
import com.tom.sportevents.core.network.model.CustomApiErrorResponse
import com.tom.sportevents.core.network.model.EventsResponse
import com.tom.sportevents.core.network.model.ScheduleResponse
import com.tom.sportevents.core.network.util.Response
import com.tom.sportevents.core.network.util.get
import io.ktor.client.HttpClient
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class NetworkDataSourceImpl @Inject constructor(
    @Named(ApiUrl) private val apiUrl: String,
    private val httpClient: HttpClient
) : NetworkDataSource {

    override suspend fun getEvents(): Response<CustomApiErrorResponse, EventsResponse> =
        httpClient.get(url = "${apiUrl}getEvents")

    override suspend fun getSchedule(): Response<CustomApiErrorResponse, ScheduleResponse> =
        httpClient.get(url = "${apiUrl}getSchedule")
}
