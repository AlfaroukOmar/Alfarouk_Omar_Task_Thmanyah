package com.thmanyah.domain.repository

import com.thmanyah.domain.models.HomeFeedState
import kotlinx.coroutines.flow.StateFlow


interface HomeRepository {
    val state: StateFlow<HomeFeedState>
    suspend fun loadInitial()
    suspend fun loadNext()
    suspend fun refresh()
}