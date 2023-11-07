package com.tom.sportevents.core.data.repository

import com.tom.sportevents.core.model.EventItem
import com.tom.sportevents.core.model.Result
import com.tom.sportevents.core.model.toDomain
import com.tom.sportevents.core.network.NetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepository @Inject constructor(private val networkDataSource: NetworkDataSource) {

    suspend fun getEvents(): Result<List<EventItem>> =
        networkDataSource.getEvents()
            .fold(
                { Result.Error() },
                { Result.Success(it.toDomain()) }
            )
}
