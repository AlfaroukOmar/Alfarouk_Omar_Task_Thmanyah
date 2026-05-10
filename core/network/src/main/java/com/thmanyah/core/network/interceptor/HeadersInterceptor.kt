package com.thmanyah.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AcceptLanguageInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request().newBuilder()
            .header("Accept-Language", "ar,en;q=0.9")
            .build()
        return chain.proceed(req)
    }
}

class UserAgentInterceptor(private val userAgent: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request().newBuilder()
            .header("User-Agent", userAgent)
            .build()
        return chain.proceed(req)
    }
}
