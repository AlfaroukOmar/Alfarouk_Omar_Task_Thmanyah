package com.thmanyah.data.repository.home

import com.thmanyah.data.repository.cache.HomeMemoryCache
import com.thmanyah.domain.models.AppendLoadState
import com.thmanyah.domain.models.InitialLoadState
import com.thmanyah.domain.repository.HomeRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val paginator: HomePaginator,
    private val cache: HomeMemoryCache,
    private val repositoryScope: RepositoryScope,
) : HomeRepository {

    override val state = paginator.state

    override suspend fun loadInitial() {
        val cached = cache.getIfFresh()
        if (cached != null) {
            paginator.restore(cached)
            repositoryScope.scope.launch {
                paginator.refresh()
                val s = paginator.state.value
                if (s.initialState == InitialLoadState.Ready) {
                    cache.put(s)
                }
            }
            return
        }
        paginator.loadInitial()
        val s = paginator.state.value
        if (s.initialState == InitialLoadState.Ready) {
            cache.put(s)
        }
    }

    override suspend fun loadNext() {
        paginator.loadNext()
        val s = paginator.state.value
        if (s.initialState == InitialLoadState.Ready && s.appendState !is AppendLoadState.Error) {
            cache.put(s)
        }
    }

    override suspend fun refresh() {
        paginator.refresh()
        val s = paginator.state.value
        if (s.initialState == InitialLoadState.Ready) {
            cache.put(s)
        }
    }
}