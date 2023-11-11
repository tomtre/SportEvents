package com.tom.sportevents.core.common.time.formater

import com.tom.sportevents.core.common.CoroutineScopeProvider
import com.tom.sportevents.core.common.time.eventsource.TimeModificationEventSourceImpl
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

// Not used in the project
class AtomicDateFormatter(
    private val timeModificationEventSource: TimeModificationEventSourceImpl,
    coroutineScopeProvider: CoroutineScopeProvider
) : DateFormatter {

    private val readWriteLock: ReadWriteLock = ReentrantReadWriteLock()

    init {
        coroutineScopeProvider.applicationScope.launch {
            timeModificationEventSource.timeZoneChanged.collect {
                setZone(ZoneId.systemDefault())
            }
        }
        coroutineScopeProvider.applicationScope.launch {
            timeModificationEventSource.localOrTimeChanged.collect {
                setLocale(Locale.getDefault())
            }
        }
    }

    @Volatile
    private var shortDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
        // Always add it
        .withZone(ZoneId.systemDefault())

    fun runWithLock(block: () -> Unit) {
        readWriteLock.readLock().lock()
        try {
            block()
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    override fun formatRelativeDays(instant: Instant): String {
        // DateUtils can't be used because it's not tread-safe.
//        return DateUtils
//            .getRelativeDateTimeString(
//                /* c = */ context,
//                /* time = */ instant.toEpochMilli(),
//                /* minResolution = */ DateUtils.DAY_IN_MILLIS,
//                /* transitionResolution = */ DateUtils.WEEK_IN_MILLIS,
//                /* flags = */ 0
//            )
//            .toString()
        TODO()
    }

    override fun currentZonedDateTime(): ZonedDateTime {
        readWriteLock.readLock().lock()
        try {
            return ZonedDateTime.now()
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

    override fun convertToZonedDateTime(instant: Instant): ZonedDateTime {
        readWriteLock.readLock().lock()
        try {
            return instant.atZone(ZoneId.systemDefault())
        } finally {
            readWriteLock.readLock().unlock()
        }
    }

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
    // BroadcastReceiver -> Intent android.intent.action.TIME_SET (plus o12/14 time changes on some phones)
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
