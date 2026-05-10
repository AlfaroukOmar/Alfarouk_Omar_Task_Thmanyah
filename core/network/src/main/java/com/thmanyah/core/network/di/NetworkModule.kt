package com.thmanyah.core.network.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.thmanyah.core.network.interceptor.AcceptLanguageInterceptor
import com.thmanyah.core.network.interceptor.UserAgentInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    @HomeBaseUrl
    fun homeBaseUrl(): String = "https://api-v2-b2sit6oh3a-uc.a.run.app/"

    @Provides
    @Singleton
    @SearchBaseUrl
    fun searchBaseUrl(): String = "https://mock.apidog.com/m1/735111-711675-default/"

    @Provides
    @Singleton
    @HomeClient
    fun homeOkHttp(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val cacheDir = File(context.cacheDir, "http_cache").apply { mkdirs() }
        val cache = Cache(cacheDir, 20L * 1024 * 1024)
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(AcceptLanguageInterceptor())
            .addInterceptor(UserAgentInterceptor("android"))
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    @SearchClient
    fun searchOkHttp(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val cacheDir = File(context.cacheDir, "http_search_cache").apply { mkdirs() }
        val cache = Cache(cacheDir, 10L * 1024 * 1024)
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(AcceptLanguageInterceptor())
            .addInterceptor(UserAgentInterceptor("android"))
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    @Named("home_retrofit")
    fun homeRetrofit(
        json: Json,
        @HomeClient client: OkHttpClient,
        @HomeBaseUrl baseUrl: String,
    ): Retrofit {
        val factory = json.asConverterFactory("application/json".toMediaType())
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(factory)
            .build()
    }

    @Provides
    @Singleton
    @Named("search_retrofit")
    fun searchRetrofit(
        json: Json,
        @SearchClient client: OkHttpClient,
        @SearchBaseUrl baseUrl: String,
    ): Retrofit {
        val factory = json.asConverterFactory("application/json".toMediaType())
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(factory)
            .build()
    }
}
