package com.tom.sportevents.core.domain

import com.tom.sportevents.core.common.time.DateTimeFormatter
import com.tom.sportevents.core.common.time.DateTimeProvider
import com.tom.sportevents.core.data.repository.EventsRepository
import com.tom.sportevents.core.model.EventItem
import com.tom.sportevents.core.model.FormattedEventItem
import com.tom.sportevents.core.model.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.isActive
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ObserveEventsUseCase @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val dateTimeFormatter: DateTimeFormatter,
    private val dateTimeProvider: DateTimeProvider
) : () -> Flow<Result<List<FormattedEventItem>>> {

    override fun invoke(): Flow<Result<List<FormattedEventItem>>> {
        // request api only once
        return flow { emit(eventsRepository.getEvents()) }
            // refresh model every time a time configuration has changed (e.g. phone time has been changed)
            .combine(dateTimeProvider.timeConfigurationChanged) { result, _ -> result }
            // refresh model every 60 seconds so "today", "yesterday", etc values are correctly computed
            .transformAndEmitLatestPeriodically(REFRESH_MODEL_INTERVAL_MILLI) { result ->
                when (result) {
                    is Result.Success -> {
                        val sortedList = result.data.sortedBy { it.date }
                        // format Instant to string date
                        val formattedList = sortedList.map { it.toFormattedEventItem(dateTimeFormatter) }
                        Result.Success(formattedList)
                    }

                    is Result.Error -> result
                }
            }
    }

    companion object {
        private const val REFRESH_MODEL_INTERVAL_MILLI = 60_000L
    }
}

fun EventItem.toFormattedEventItem(dateTimeFormatter: DateTimeFormatter): FormattedEventItem =
    FormattedEventItem(
        id = id,
        title = title,
        subtitle = subtitle,
        formattedDate = dateTimeFormatter.format(date),
        imageUrl = imageUrl,
        videoUrl = videoUrl
    )

@OptIn(ExperimentalCoroutinesApi::class)
private fun <T, V> Flow<T>.transformAndEmitLatestPeriodically(intervalMilli: Long, block: (T) -> V): Flow<V> = transformLatest {
    while (coroutineContext.isActive) {
        emit(block(it))
        delay(intervalMilli)
    }
}
