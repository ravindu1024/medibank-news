package com.medibank.data.api

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor(private val apiKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if(request.method == "GET"){
            val url = request.url.newBuilder().addQueryParameter("apiKey", apiKey).build()
            val authRequest = request.newBuilder().url(url).build()
            return chain.proceed(authRequest)
        }

        return chain.proceed(request)
    }
}