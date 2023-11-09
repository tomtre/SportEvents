package com.tom.sportevents.core.common.time

import com.tom.sportevents.core.common.BehaviorFlow
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

interface TimeManager {
    val timeConfigurationChanged: BehaviorFlow<Unit>

    fun formatInstant(instant: Instant): String

    fun formatLocalDate(localDate: LocalDate): String

    fun formatLocalDateList(localDates: List<LocalDate>): List<String>

    fun formatLocalDateTime(localDateTime: LocalDateTime): String

    fun formatLocalDateTimeList(localDateTimeList: List<LocalDateTime>): List<String>

    fun formatRelativeDays(instant: Instant): String

    fun parseFromIsoInstantFormat(input: String): Instant

    fun currentZonedDateTime(): ZonedDateTime

    fun convertToZonedDateTime(instant: Instant): ZonedDateTime
}
