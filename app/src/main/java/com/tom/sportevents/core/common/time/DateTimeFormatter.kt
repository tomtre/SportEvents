package com.tom.sportevents.core.common.time

import android.content.Context
import android.text.format.DateUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import javax.inject.Inject

class DateTimeFormatter @Inject constructor(@ApplicationContext private val context: Context) {

    fun format(instant: Instant): String {
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
}
