package com.thmanyah.data.repository.home

import com.thmanyah.domain.entities.ContentItem
import com.thmanyah.domain.entities.HomeSection

object HomeFeedMerger {
    /**
     * Appends [incoming] sections as new home rows for page [page]. Each section id is suffixed so
     * repeated layouts/titles from later pages do not collide with earlier blocks.
     */
    fun appendPagedSections(
        existing: List<HomeSection>,
        incoming: List<HomeSection>,
        page: Int,
    ): List<HomeSection> {
        if (incoming.isEmpty()) return existing
        val appended = incoming.mapIndexed { index, section ->
            section.copy(id = "${section.id}_page${page}_$index")
        }
        return existing + appended
    }

    fun mergeSections(
        existing: List<HomeSection>,
        incoming: List<HomeSection>,
        dedupeMergedItems: Boolean = true,
    ): List<HomeSection> {
        val byId = incoming.associateBy { it.id }
        return existing.map { old ->
            val neu = byId[old.id]
            if (neu == null) {
                old
            } else {
                old.copy(items = mergeItems(old.items, neu.items, dedupeMergedItems))
            }
        }
    }

    fun mergeItems(
        a: List<ContentItem>,
        b: List<ContentItem>,
        dedupeById: Boolean = true,
    ): List<ContentItem> {
        if (!dedupeById) return a + b
        val seen = LinkedHashSet<String>()
        val out = ArrayList<ContentItem>(a.size + b.size)
        for (item in a) {
            if (seen.add(item.id)) out.add(item)
        }
        for (item in b) {
            if (seen.add(item.id)) out.add(item)
        }
        return out
    }
}