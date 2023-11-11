package com.tom.sportevents.fake

import com.tom.sportevents.core.common.MutableBehaviorFlow
import com.tom.sportevents.core.common.time.eventsource.TimeModificationEventSource
import kotlinx.coroutines.flow.Flow

class TestTimeModificationEventSource : TimeModificationEventSource {

    private val _timeZoneChanged = MutableBehaviorFlow<Unit>()
    override val timeZoneChanged: Flow<Unit>
        get() = _timeZoneChanged

    private val _localOrTimeChanged = MutableBehaviorFlow<Unit>()
    override val localOrTimeChanged: Flow<Unit>
        get() = _localOrTimeChanged

    private val _timeModificationEvent = MutableBehaviorFlow<Unit>()
    override val timeModificationEvent: Flow<Unit>
        get() = _timeModificationEvent

    suspend fun emitTimeModificationEvent() {
        _timeModificationEvent.emit(Unit)
    }
}
