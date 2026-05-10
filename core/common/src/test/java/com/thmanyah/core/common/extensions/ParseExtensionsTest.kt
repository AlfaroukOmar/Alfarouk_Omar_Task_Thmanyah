package com.thmanyah.core.common.extensions

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class ParseExtensionsTest {

    @Test
    fun toIntSafe_handlesMixed() {
        assertThat("42".toIntSafe()).isEqualTo(42)
        assertThat("order3".toIntSafe()).isNull()
        assertThat(null.toIntSafe()).isNull()
    }

    @Test
    fun duration_capsNegative() {
        assertThat((-1).toDurationSecondsSafe()).isNull()
        assertThat(100L.toDurationSecondsSafe()).isEqualTo(100L)
        assertThat(100.toDurationSecondsSafe()).isEqualTo(100L)
    }

    @Test
    fun parseDate_isoDate() {
        assertThat("2024-01-15".parseReleaseDate()).isNotNull()
    }
}
