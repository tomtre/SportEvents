package com.tom.sportevents.core.domain

import com.tom.sportevents.core.data.repository.EventsRepository
import com.tom.sportevents.core.model.EventItem
import com.tom.sportevents.core.model.Result
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val eventsRepository: EventsRepository
) : suspend () -> Result<List<EventItem>> {

    override suspend fun invoke(): Result<List<EventItem>> =
        eventsRepository.getEvents()
}
