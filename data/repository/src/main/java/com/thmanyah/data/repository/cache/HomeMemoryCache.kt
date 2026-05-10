package com.thmanyah.data.repository.cache

import com.thmanyah.domain.models.HomeFeedState
import com.thmanyah.domain.models.InitialLoadState
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeMemoryCache @Inject constructor() {
    private val mutex = Mutex()
    private var entry: Pair<HomeFeedState, Long>? = null
    private val ttlMs = 5 * 60 * 1000L

    suspend fun getIfFresh(): HomeFeedState? = mutex.withLock {
        val now = System.currentTimeMillis()
        val e = entry ?: return null
        if (now - e.second > ttlMs) {
            entry = null
            return null
        }
        e.first.takeIf { it.initialState == InitialLoadState.Ready && it.sections.isNotEmpty() }
    }

    suspend fun put(state: HomeFeedState) = mutex.withLock {
        if (state.initialState == InitialLoadState.Ready && state.sections.isNotEmpty()) {
            entry = state to System.currentTimeMillis()
        }
    }

    suspend fun clear() = mutex.withLock { entry = null }
}
