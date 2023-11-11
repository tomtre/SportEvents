package com.tom.sportevents.core.common.time.eventsource

import com.tom.sportevents.core.common.CoroutineScopeProvider
import com.tom.sportevents.core.common.MutableBehaviorFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimeModificationEventSourceImpl @Inject constructor(
    private val timeModificationBroadcastReceiver: TimeModificationBroadcastReceiver,
    private val coroutineScopeProvider: CoroutineScopeProvider
) : TimeModificationEventSource {

    private val _timeZoneChanged = MutableBehaviorFlow<Unit>()
    override val timeZoneChanged: Flow<Unit> = _timeZoneChanged

    private val _localOrTimeChanged = MutableBehaviorFlow<Unit>()
    override val localOrTimeChanged: Flow<Unit> = _localOrTimeChanged

    override val timeModificationEvent: Flow<Unit> = combine(_timeZoneChanged, _localOrTimeChanged) { _, _ -> Unit }

    init {
        timeModificationBroadcastReceiver.register(
            onTimeZoneChanged = {
                coroutineScopeProvider.applicationScope.launch { _timeZoneChanged.emit(Unit) }
            },
            onLocalAndTimeSetChanged = {
                coroutineScopeProvider.applicationScope.launch { _localOrTimeChanged.emit(Unit) }
            }
        )
    }

    // In most case it's not necessary to clear
    fun clear() {
        timeModificationBroadcastReceiver.unregister()
    }
}
