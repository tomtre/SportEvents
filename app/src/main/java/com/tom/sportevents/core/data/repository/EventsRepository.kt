package com.tom.sportevents.core.data.repository

import com.tom.sportevents.core.common.time.TimeManager
import com.tom.sportevents.core.model.EventItem
import com.tom.sportevents.core.model.Result
import com.tom.sportevents.core.model.toDomain
import com.tom.sportevents.core.network.NetworkDataSource
import java.time.format.DateTimeParseException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val timeManager: TimeManager
) {

    suspend fun getEvents(): Result<List<EventItem>> =
        networkDataSource.getEvents()
            .fold(
                ifLeft = { Result.Error() },
                ifRight = {
                    try {
                        Result.Success(it.toDomain(timeManager))
                    } catch (e: DateTimeParseException) {
                        Result.Error(e)
                    }
                }
            )
}
