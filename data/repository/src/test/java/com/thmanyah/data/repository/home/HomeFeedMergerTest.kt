package com.thmanyah.data.repository.home

import com.google.common.truth.Truth.assertThat
import com.thmanyah.core.fixtures.ContentItemSamples
import com.thmanyah.core.fixtures.HomeSectionSamples
import com.thmanyah.domain.entities.ContentItem
import org.junit.Test

class HomeFeedMergerTest {

    @Test
    fun mergeItems_preservesOrderAndDedupes() {
        val a = ContentItemSamples.episode(id = "1")
        val b = ContentItemSamples.episode(id = "2")
        val c = ContentItemSamples.episode(id = "1", title = "other")
        val out = HomeFeedMerger.mergeItems(listOf(a, b), listOf(c), dedupeById = true)
        assertThat(out.map { it.id }).containsExactly("1", "2").inOrder()
        assertThat((out[0] as ContentItem.Episode).title).isEqualTo(a.title)
    }

    @Test
    fun mergeItems_withoutDedupe_keepsDuplicateIds() {
        val a = ContentItemSamples.episode(id = "1", title = "first")
        val b = ContentItemSamples.episode(id = "1", title = "second")
        val out = HomeFeedMerger.mergeItems(listOf(a), listOf(b), dedupeById = false)
        assertThat(out.map { it.id }).containsExactly("1", "1").inOrder()
        assertThat((out[0] as ContentItem.Episode).title).isEqualTo("first")
        assertThat((out[1] as ContentItem.Episode).title).isEqualTo("second")
    }

    @Test
    fun appendPagedSections_appendsWithUniqueIds() {
        val base = HomeSectionSamples.twoLinesGridSection()
        val page2Block = base.copy(items = listOf(ContentItemSamples.episode(id = "ep-page2-only")))
        val out = HomeFeedMerger.appendPagedSections(listOf(base), listOf(page2Block), page = 2)
        assertThat(out).hasSize(2)
        assertThat(out[0].id).isEqualTo(base.id)
        assertThat(out[1].id).isNotEqualTo(base.id)
        assertThat(out[1].items.map { it.id }).containsExactly("ep-page2-only")
    }

    @Test
    fun mergeSections_mergesItemsForMatchingSectionIds() {
        val base = HomeSectionSamples.twoLinesGridSection()
        val extra = ContentItemSamples.episode(id = "ep-new", title = "Extra")
        val inc = base.copy(items = listOf(extra))
        val merged = HomeFeedMerger.mergeSections(listOf(base), listOf(inc), dedupeMergedItems = true)
        assertThat(merged.single().items.map { it.id }).contains("ep-new")
    }

    @Test
    fun mergeSections_withoutItemDedupe_preservesDuplicateIdsInExistingWhenMerging() {
        val dup = ContentItemSamples.episode(id = "same", title = "first")
        val dup2 = ContentItemSamples.episode(id = "same", title = "second")
        val base = HomeSectionSamples.twoLinesGridSection().copy(items = listOf(dup, dup2))
        val extra = ContentItemSamples.episode(id = "ep-new", title = "Extra")
        val inc = base.copy(items = listOf(extra))
        val merged = HomeFeedMerger.mergeSections(listOf(base), listOf(inc), dedupeMergedItems = false)
        assertThat(merged.single().items.map { it.id })
            .containsExactly("same", "same", "ep-new")
            .inOrder()
    }
}