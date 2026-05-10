package com.thmanyah.data.remote.di

import com.thmanyah.data.remote.api.HomeApi
import com.thmanyah.data.remote.api.SearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    @Singleton
    fun homeApi(@Named("home_retrofit") retrofit: Retrofit): HomeApi = retrofit.create(HomeApi::class.java)

    @Provides
    @Singleton
    fun searchApi(@Named("search_retrofit") retrofit: Retrofit): SearchApi = retrofit.create(SearchApi::class.java)
}