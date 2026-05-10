package com.thmanyah.core.ui.util

import android.content.res.Resources
import com.thmanyah.core.ui.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn


fun Resources.formatDurationShort(seconds: Long?): String? {
    if (seconds == null) return null
    val s = seconds.coerceAtLeast(0L)
    val h = s / 3600
    val m = (s % 3600) / 60
    return when {
        h > 0 && m > 0 -> getString(R.string.duration_hours_minutes, h, m)
        h > 0 -> getString(R.string.duration_hours_only, h)
        else -> getString(R.string.duration_minutes_only, m)
    }
}

/**
 * Day-granularity relative label from a publish [LocalDate] (API does not provide time-of-day).
 */
fun Resources.formatRelativePublishDate(date: LocalDate?): String? =
    formatRelativePublishDateFrom(
        today = kotlin.time.Clock.System.todayIn(TimeZone.currentSystemDefault()),
        date = date,
    )

internal fun Resources.formatRelativePublishDateFrom(today: LocalDate, date: LocalDate?): String? {
    if (date == null) return null
    val days = (today.toEpochDays() - date.toEpochDays()).toInt()
    return when {
        days <= 0 -> getString(R.string.relative_today)
        days == 1 -> getString(R.string.relative_yesterday)
        days == 2 -> getString(R.string.relative_two_days_ago)
        else -> getQuantityString(R.plurals.relative_days_ago, days, days)
    }
}
fun Resources.episodesCountLabel(count: Int): String =
    getQuantityString(R.plurals.episodes_count_label, count, count)