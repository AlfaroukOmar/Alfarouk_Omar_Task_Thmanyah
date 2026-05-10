package com.thmanyah.data.repository.di

import com.thmanyah.data.repository.home.HomeRepositoryImpl
import com.thmanyah.data.repository.search.SearchRepositoryImpl
import com.thmanyah.domain.repository.HomeRepository
import com.thmanyah.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindModule {
    @Binds
    @Singleton
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository
}
