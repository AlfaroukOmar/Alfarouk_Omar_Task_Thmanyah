package com.thmanyah.core.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HomeBaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SearchBaseUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HomeClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SearchClient