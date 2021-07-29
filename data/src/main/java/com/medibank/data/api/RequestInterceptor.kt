package com.medibank.data.api

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds the api key as a query param to GET api calls if the api call has the apiKey param added.
 * ie: the api calls where a key is required
 *
 * This can be easily modified to handle multiple api keys or keys for different sources
 */
class RequestInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.url.queryParameter("apiKey") != null) {

            val url = request.url
                .newBuilder()
                .setQueryParameter("apiKey", apiKey).build()

            val authRequest = request.newBuilder().url(url).build()
            return chain.proceed(authRequest)
        }

        return chain.proceed(request)
    }
}