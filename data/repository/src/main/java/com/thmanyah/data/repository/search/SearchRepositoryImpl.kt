package com.thmanyah.data.repository.search

import com.thmanyah.core.common.di.SettingsRepository
import com.thmanyah.core.common.dispatcher.IoDispatcher
import com.thmanyah.core.network.error.ErrorMapper
import com.thmanyah.data.remote.api.SearchApi
import com.thmanyah.data.remote.dto.SearchResponseDto
import com.thmanyah.data.remote.mapper.SectionMapper
import com.thmanyah.domain.entities.HomeSection
import com.thmanyah.domain.models.AppResult
import com.thmanyah.domain.repository.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val api: SearchApi,
    private val mapper: SectionMapper,
    private val settingsRepository: SettingsRepository,
    @param:IoDispatcher private val io: CoroutineDispatcher,
) : SearchRepository {
    override fun search(query: String): Flow<AppResult<List<HomeSection>>> = flow {
        val result = withContext(io) {
            val dedupeWhenMapping = settingsRepository.dedupeContentIdsWhenMapping.first()
            runCatching { api.search(query) }.fold(
                onSuccess = { dto: SearchResponseDto ->
                    val sections = dto.sections.mapNotNull { mapper.mapSection(it, dedupeWhenMapping) }
                    AppResult.Success(sections)
                },
                onFailure = { AppResult.Failure(ErrorMapper.map(it)) },
            )
        }
        emit(result)
    }
}
