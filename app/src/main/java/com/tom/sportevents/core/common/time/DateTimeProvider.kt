package com.tom.sportevents.core.common.time

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateTimeProvider @Inject constructor(
    timeModificationsBroadcastReceiver: TimeModificationsBroadcastReceiver
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _timeConfigurationChanged = MutableStateFlow(Unit)
    val timeConfigurationChanged = _timeConfigurationChanged.asStateFlow()

    init {
        timeModificationsBroadcastReceiver.register(
            setOnTimeSettingsChanged = {
                coroutineScope.launch {
                    _timeConfigurationChanged.emit(Unit)
                }
            }
        )
    }

    fun currentInstant(): Instant =
        Instant.now()
}
