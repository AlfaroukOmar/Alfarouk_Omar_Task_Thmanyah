package com.thmanyah.data.repository.home

import com.google.common.truth.Truth.assertThat
import com.thmanyah.core.fixtures.ContentItemSamples
import org.junit.Test

class MergeItemsTest {
    @Test
    fun dedupesById_viaMerger() {
        val p = ContentItemSamples.podcast(id = "1", title = "a")
        val out = HomeFeedMerger.mergeItems(listOf(p), listOf(p), dedupeById = true)
        assertThat(out).hasSize(1)
    }

    @Test
    fun withoutDedupe_keepsDuplicateItems() {
        val p = ContentItemSamples.podcast(id = "1", title = "a")
        val out = HomeFeedMerger.mergeItems(listOf(p), listOf(p), dedupeById = false)
        assertThat(out).hasSize(2)
    }
}