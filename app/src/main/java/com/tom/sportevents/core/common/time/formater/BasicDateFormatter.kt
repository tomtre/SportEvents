package com.tom.sportevents.core.common.time.formater

import android.content.Context
import android.text.format.DateUtils
import androidx.annotation.MainThread
import com.tom.sportevents.core.common.CoroutineScopeProvider
import com.tom.sportevents.core.common.time.eventsource.TimeModificationEventSourceImpl
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import javax.inject.Inject

/**
 * Not thread safe implementation. All operations should be performed on the main thread.
 * Use the AtomicDateFormatter implementation for thread-safety.
 * */

@MainThread
class BasicDateFormatter @Inject constructor(
    private val timeModificationEventSource: TimeModificationEventSourceImpl,
    coroutineScopeProvider: CoroutineScopeProvider,
    @ApplicationContext private val context: Context
) : DateFormatter {

    private var shortDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        .withLocale(Locale.getDefault())
        // Always add it
        .withZone(ZoneId.systemDefault())

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

    override fun formatRelativeDays(instant: Instant): String {
        // No control of the date format. No control of current time.
        // In commercial project a better solution should be used.
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

    override fun currentZonedDateTime(): ZonedDateTime =
        ZonedDateTime.now()

    override fun convertToZonedDateTime(instant: Instant): ZonedDateTime =
        instant.atZone(ZoneId.systemDefault())

    // BroadcastReceiver -> Intent android.intent.action.TIMEZONE_CHANGED
    // Don't forget to refresh ui states if the view model is prepared in a VM, not in a View
    private fun setZone(zoneId: ZoneId) {
        shortDateTimeFormatter = shortDateTimeFormatter.withZone(zoneId)
    }

    // BroadcastReceiver -> Intent android.intent.action.LOCALE_CHANGED
    // BroadcastReceiver -> Intent android.intent.action.TIME_SET (plus o12/14 time changes on some phones)
    // Don't forget to refresh ui states if the view model is prepared in a VM, not in a View
    private fun setLocale(locale: Locale) {
        shortDateTimeFormatter = shortDateTimeFormatter.withLocale(locale)
    }
}
