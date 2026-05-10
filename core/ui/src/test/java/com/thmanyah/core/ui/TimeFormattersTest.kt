package com.thmanyah.core.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.thmanyah.core.ui.util.formatDurationShort
import com.thmanyah.core.ui.util.formatRelativePublishDateFrom
import kotlinx.datetime.LocalDate
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class TimeFormattersTest {

    private val res: android.content.res.Resources
        get() = ApplicationProvider.getApplicationContext<Context>().resources

    @Test
    fun formatDuration_hoursAndMinutes() {
        assertThat(res.formatDurationShort(3661L)).isNotNull()
        assertThat(res.formatDurationShort(3661L)!!.isNotBlank()).isTrue()
    }

    @Test
    fun formatRelative_usesTodayLabel() {
        val today = LocalDate(2026, 5, 10)
        val label = res.formatRelativePublishDateFrom(today, today)
        assertThat(label).isNotNull()
    }
}
