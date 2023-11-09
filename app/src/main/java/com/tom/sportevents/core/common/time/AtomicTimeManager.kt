package com.tom.sportevents.core.common.time

import android.content.Context
import android.text.format.DateUtils
import com.tom.sportevents.core.common.BehaviorFlow
import com.tom.sportevents.core.common.CoroutineScopeProvider
import com.tom.sportevents.core.common.MutableBehaviorFlow
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.inject.Inject

@Suppress("TooManyFunctions")
class AtomicTimeManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val coroutineScopeProvider: CoroutineScopeProvider,
    timeModificationsBroadcastReceiver: TimeModificationsBroadcastReceiver
) : TimeManager {

    private val _timeConfigurationChanged = MutableBehaviorFlow(Unit)
    override val timeConfigurationChanged: BehaviorFlow<Unit> = _timeConfigurationChanged

    private val readWriteLock: ReadWriteLock = ReentrantReadWriteLock()

    @Volatile
    private var shortDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
        // Always add it
        .withZone(ZoneId.systemDefault())

    private val isoInstantDateTimeFormatter = DateTimeFormatter.ISO_INSTANT

    init {
        timeModificationsBroadcastReceiver.register(
            onTimeZoneChanged = {
                setZone(ZoneId.systemDefault())
                broadcastTimeConfigurationChanged()
            },
            onLocalAndTimeSetChanged = {
                setLocale(Locale.getDefault())
                broadcastTimeConfigurationChanged()
            }
        )
    }

    private fun broadcastTimeConfigurationChanged() {
        coroutineScopeProvider.applicationScope.launch {
            _timeConfigurationChanged.emit(Unit)
        }
    }

    override fun formatInstant(instant: Instant): String {
        readWriteLock.readLock().lock()
        try {
            return shortDateTimeFormatter.format(instant)
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    override fun formatLocalDate(localDate: LocalDate): String {
        readWriteLock.readLock().lock()
        try {
            return shortDateTimeFormatter.format(localDate)
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    override fun formatLocalDateList(localDates: List<LocalDate>): List<String> {
        readWriteLock.readLock().lock()
        try {
            return localDates.map { shortDateTimeFormatter.format(it) }
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    override fun formatLocalDateTime(localDateTime: LocalDateTime): String {
        readWriteLock.readLock().lock()
        try {
            return shortDateTimeFormatter.format(localDateTime)
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    override fun formatLocalDateTimeList(localDateTimeList: List<LocalDateTime>): List<String> {
        readWriteLock.readLock().lock()
        try {
            return localDateTimeList.map { shortDateTimeFormatter.format(it) }
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    override fun formatRelativeDays(instant: Instant): String {
        return DateUtils
            .getRelativeDateTimeString(
                /* c = */ context,
                /* time = */ instant.toEpochMilli(),
                /* minResolution = */ DateUtils.DAY_IN_MILLIS,
                /* transitionResolution = */ DateUtils.WEEK_IN_MILLIS,
                /* flags = */ 0
            )
            .toString()
    }

    override fun parseFromIsoInstantFormat(input: String): Instant =
        Instant.from(isoInstantDateTimeFormatter.parse(input))

    override fun currentZonedDateTime(): ZonedDateTime =
        ZonedDateTime.now()

    override fun convertToZonedDateTime(instant: Instant): ZonedDateTime =
        instant.atZone(ZoneId.systemDefault())

    // BroadcastReceiver -> Intent android.intent.action.TIMEZONE_CHANGED
    // Don't forget to refresh ui states if the view model is prepared in a VM, not in a View
    private fun setZone(zoneId: ZoneId) {
        readWriteLock.writeLock().lock()
        try {
            // Reset all timezone aware formatters and parsers here
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
            // Reset all local and time format aware formatters and parsers here
            shortDateTimeFormatter = shortDateTimeFormatter.withLocale(locale)
        } finally {
            readWriteLock.writeLock().unlock()
        }
    }
}
