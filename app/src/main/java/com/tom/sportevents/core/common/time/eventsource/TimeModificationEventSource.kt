package com.tom.sportevents.core.common.time.eventsource

import kotlinx.coroutines.flow.Flow

interface TimeModificationEventSource {
    val timeZoneChanged: Flow<Unit>
    val localOrTimeChanged: Flow<Unit>
    val timeModificationEvent: Flow<Unit>
}
