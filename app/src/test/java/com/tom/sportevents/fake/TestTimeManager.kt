package com.tom.sportevents.fake

import com.tom.sportevents.core.common.BehaviorFlow
import com.tom.sportevents.core.common.MutableBehaviorFlow
import com.tom.sportevents.core.common.time.TimeManager
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

class TestTimeManager(
    private var currentZonedDateTime: ZonedDateTime = ZonedDateTime.now(),
    private val formatRelativeDays: String = "Tomorrow"
) : TimeManager {

    private val _timeConfigurationChanged = MutableBehaviorFlow(Unit)

    override val timeConfigurationChanged: BehaviorFlow<Unit>
        get() = _timeConfigurationChanged

    /**
     * A test-only API
     */
    suspend fun emitConfigurationChanged() {
        _timeConfigurationChanged.emit(Unit)
    }

    override fun formatInstant(instant: Instant): String {
        TODO("Not yet implemented")
    }

    override fun formatLocalDate(localDate: LocalDate): String {
        TODO("Not yet implemented")
    }

    override fun formatLocalDateList(localDates: List<LocalDate>): List<String> {
        TODO("Not yet implemented")
    }

    override fun formatLocalDateTime(localDateTime: LocalDateTime): String {
        TODO("Not yet implemented")
    }

    override fun formatLocalDateTimeList(localDateTimeList: List<LocalDateTime>): List<String> {
        TODO("Not yet implemented")
    }

    override fun formatRelativeDays(instant: Instant): String =
        formatRelativeDays

    override fun parseFromIsoInstantFormat(input: String): Instant {
        TODO("Not yet implemented")
    }

    /**
     * A test-only API
     */
    fun setCurrentZonedDateTime(zonedDateTime: ZonedDateTime) {
        currentZonedDateTime = zonedDateTime
    }

    override fun currentZonedDateTime(): ZonedDateTime =
        currentZonedDateTime

    override fun convertToZonedDateTime(instant: Instant): ZonedDateTime {
        TODO("Not yet implemented")
    }
}
