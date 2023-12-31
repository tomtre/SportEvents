package com.tom.sportevents.core.domain

import com.tom.sportevents.core.common.time.eventsource.TimeModificationEventSource
import com.tom.sportevents.core.common.time.formater.DateFormatter
import com.tom.sportevents.core.data.repository.ScheduleRepository
import com.tom.sportevents.core.model.FormattedScheduleItem
import com.tom.sportevents.core.model.Result
import com.tom.sportevents.core.model.ScheduleItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import java.time.LocalTime
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ObserveScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val timeModificationEventSource: TimeModificationEventSource,
    private val dateFormatter: DateFormatter
) : () -> Flow<Result<List<FormattedScheduleItem>>> {

    override fun invoke(): Flow<Result<List<FormattedScheduleItem>>> {
        return fetchDataInLoopFlow()
            // reformat model every time a time configuration has changed (e.g. phone time or time zone has been changed)
            .combine(timeModificationEventSource.timeModificationEvent.onStart { emit(Unit) }) { result, _ ->
                sortAndFormat(result)
            }
            .distinctUntilChanged()
    }

    private fun fetchDataInLoopFlow(): Flow<Result<List<ScheduleItem>>> {
        return flow {
            var successfullyFetched = false
            while (coroutineContext.isActive) {
                when (val result = scheduleRepository.getSchedule()) {
                    is Result.Success -> {
                        successfullyFetched = true
                        emit(result)
                    }

                    is Result.Error -> {
                        // emit an error only if the data hasn't been successfully fetched yet
                        if (!successfullyFetched) {
                            emit(result)
                        }
                    }
                }
                delay(REFRESH_API_REQUEST_DELAY_MILLIS)
            }
        }
    }

    private fun sortAndFormat(result: Result<List<ScheduleItem>>) =
        when (result) {
            is Result.Success -> {
                val tomorrowList = result.data.filterTomorrow(dateFormatter)
                val sortedList = tomorrowList.sortedBy { it.date }
                // format Instant to relative date as String
                val formattedList = sortedList.map { it.toFormattedScheduleItem(dateFormatter) }
                Result.Success(formattedList)
            }

            is Result.Error -> result
        }

    private fun List<ScheduleItem>.filterTomorrow(dateFormatter: DateFormatter): List<ScheduleItem> {
        val tomorrowZoned = dateFormatter.currentZonedDateTime().plusDays(1)
        val tomorrowStarInstant = tomorrowZoned.with(LocalTime.MIN).toInstant()
        val tomorrowEndInstant = tomorrowZoned.with(LocalTime.MAX).toInstant()
        return this.filter {
/*
            We can't use isAfter and isBefore because time intervals should be half-open:
            https://wrschneider.github.io/2014/01/07/time-intervals-and-other-ranges-should.html
            val dateZoned = timeManager.convertToZonedDateTime(it.date)
            dateZoned.isAfter(tomorrowStartZoned) && dateZoned.isBefore(tomorrowEndZoned)
*/
            val dateEpochMilli = it.date.toEpochMilli()
            tomorrowStarInstant.toEpochMilli() <= dateEpochMilli && dateEpochMilli <= tomorrowEndInstant.toEpochMilli()
        }
    }

    private fun ScheduleItem.toFormattedScheduleItem(dateFormatter: DateFormatter): FormattedScheduleItem =
        FormattedScheduleItem(
            id = id,
            title = title,
            subtitle = subtitle,
            formattedDate = dateFormatter.formatRelativeDays(date),
            imageUrl = imageUrl
        )

    companion object {
        private const val REFRESH_API_REQUEST_DELAY_MILLIS = 30_000L
    }
}
