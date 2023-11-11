package com.tom.sportevents.core.common.time.eventsource

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TimeModificationBroadcastReceiver @Inject constructor(
    @ApplicationContext val context: Context
) : BroadcastReceiver() {

    private var registered = false

    private var timeZoneChangedListener: (() -> Unit)? = null
    private var localAndTimeSetChangedListener: (() -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        if (intent.action == Intent.ACTION_TIMEZONE_CHANGED) {
            timeZoneChangedListener?.let { it() }
        }
        if (intent.action == Intent.ACTION_LOCALE_CHANGED ||
            intent.action == "android.intent.action.TIME_SET"
        ) {
            localAndTimeSetChangedListener?.let { it() }
        }
    }

    fun register(onTimeZoneChanged: () -> Unit, onLocalAndTimeSetChanged: () -> Unit) {
        if (!registered) {
            timeZoneChangedListener = onTimeZoneChanged
            localAndTimeSetChangedListener = onLocalAndTimeSetChanged
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
            filter.addAction(Intent.ACTION_LOCALE_CHANGED)
            filter.addAction("android.intent.action.TIME_SET")
            context.registerReceiver(this, filter)
            registered = true
        }
    }

    fun unregister() {
        if (registered) {
            timeZoneChangedListener = null
            localAndTimeSetChangedListener = null
            context.unregisterReceiver(this)
            registered = false
        }
    }
}
