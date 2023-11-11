package com.tom.sportevents.core.common.time.formater

import java.time.Instant
import java.time.ZonedDateTime

interface DateFormatter {
    fun formatRelativeDays(instant: Instant): String
    fun currentZonedDateTime(): ZonedDateTime
    fun convertToZonedDateTime(instant: Instant): ZonedDateTime
}
