package com.tom.sportevents.core.common.time

import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DateParser @Inject constructor() {

    private val isoInstantDateTimeFormatter = DateTimeFormatter.ISO_INSTANT

    fun parseFromIsoInstantFormat(input: String): Instant =
        Instant.from(isoInstantDateTimeFormatter.parse(input))
}
