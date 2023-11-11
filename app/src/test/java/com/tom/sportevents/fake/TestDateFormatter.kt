package com.tom.sportevents.fake

import com.tom.sportevents.core.common.time.formater.DateFormatter
import java.time.Instant
import java.time.ZonedDateTime

class TestDateFormatter(
    private var currentZonedDateTime: ZonedDateTime = ZonedDateTime.now(),
    private val formatRelativeDays: String = "Tomorrow"
) : DateFormatter {

    override fun formatRelativeDays(instant: Instant): String =
        formatRelativeDays

    fun setCurrentZonedDateTime(zonedDateTime: ZonedDateTime) {
        currentZonedDateTime = zonedDateTime
    }

    override fun currentZonedDateTime(): ZonedDateTime =
        currentZonedDateTime

    override fun convertToZonedDateTime(instant: Instant): ZonedDateTime {
        TODO("Not yet implemented")
    }
}
