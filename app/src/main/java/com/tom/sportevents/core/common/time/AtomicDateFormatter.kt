package com.tom.sportevents.core.common.time

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AtomicDateFormatter @Inject constructor(
    timeModificationsBroadcastReceiver: TimeModificationsBroadcastReceiver
) {
    private val readWriteLock: ReadWriteLock = ReentrantReadWriteLock()
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    init {
        timeModificationsBroadcastReceiver.register(
            setOnTimeSettingsChanged = {
                setZone(ZoneId.systemDefault())
                setLocale(Locale.getDefault())
                coroutineScope.launch {
                    _timeConfigurationChanged.emit(Unit)
                }
            }
        )
    }

    private val _timeConfigurationChanged = MutableStateFlow(Unit)
    val timeConfigurationChanged = _timeConfigurationChanged.asStateFlow()

    @Volatile
    private var shortDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
        // Always add it
        .withZone(ZoneId.systemDefault())

    @JvmName("formatLocalDate")
    fun format(localDate: LocalDate): String {
        readWriteLock.readLock().lock()
        try {
            return shortDateTimeFormatter.format(localDate)
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    @JvmName("formatLocalDateList")
    fun format(localDates: List<LocalDate>): List<String> {
        readWriteLock.readLock().lock()
        try {
            return localDates.map { shortDateTimeFormatter.format(it) }
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    @JvmName("formatLocalDateTime")
    fun format(localDateTime: LocalDateTime): String {
        readWriteLock.readLock().lock()
        try {
            return shortDateTimeFormatter.format(localDateTime)
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    @JvmName("formatLocalDateTimeList")
    fun format(localDateTimeList: List<LocalDateTime>): List<String> {
        readWriteLock.readLock().lock()
        try {
            return localDateTimeList.map { shortDateTimeFormatter.format(it) }
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    // BroadcastReceiver -> Intent android.intent.action.TIMEZONE_CHANGED
    // Don't forget to refresh ui states if the view model is prepared in a VM, not in a View
    private fun setZone(zoneId: ZoneId) {
        readWriteLock.writeLock().lock()
        try {
            shortDateTimeFormatter = shortDateTimeFormatter.withZone(zoneId)
        } finally {
            readWriteLock.writeLock().unlock()
        }
    }

    // BroadcastReceiver -> Intent android.intent.action.LOCALE_CHANGED
    // BroadcastReceiver -> Intent android.intent.action.TIME_SET (12/14 time)
    // Don't forget to refresh ui states if the view model is prepared in a VM, not in a View
    private fun setLocale(locale: Locale) {
        readWriteLock.writeLock().lock()
        try {
            shortDateTimeFormatter = shortDateTimeFormatter.withLocale(locale)
        } finally {
            readWriteLock.writeLock().unlock()
        }
    }
}
