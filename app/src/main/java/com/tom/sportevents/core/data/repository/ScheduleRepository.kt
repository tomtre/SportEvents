package com.tom.sportevents.core.data.repository

import com.tom.sportevents.core.common.time.DateParser
import com.tom.sportevents.core.model.Result
import com.tom.sportevents.core.model.ScheduleItem
import com.tom.sportevents.core.model.toDomain
import com.tom.sportevents.core.network.NetworkDataSource
import java.time.format.DateTimeParseException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val dateParser: DateParser
) {

    suspend fun getSchedule(): Result<List<ScheduleItem>> =
        networkDataSource.getSchedule()
            .fold(
                { Result.Error() },
                {
                    try {
                        Result.Success(it.toDomain(dateParser))
                    } catch (e: DateTimeParseException) {
                        Result.Error(e)
                    }
                }
            )
}
