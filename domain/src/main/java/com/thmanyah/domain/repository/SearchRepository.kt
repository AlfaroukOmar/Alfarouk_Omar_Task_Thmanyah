package com.thmanyah.domain.repository

import com.thmanyah.domain.models.AppResult
import com.thmanyah.domain.entities.HomeSection
import kotlinx.coroutines.flow.Flow

fun interface SearchRepository {
    fun search(query: String): Flow<AppResult<List<HomeSection>>>
}