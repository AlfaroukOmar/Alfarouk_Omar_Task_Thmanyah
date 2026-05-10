package com.thmanyah.domain.entities

import com.thmanyah.domain.models.ContentType
import com.thmanyah.domain.models.SectionLayout

data class HomeSection(
    val id: String,
    val title: String,
    val layout: SectionLayout,
    val contentType: ContentType,
    val order: Int,
    val items: List<ContentItem>,
)