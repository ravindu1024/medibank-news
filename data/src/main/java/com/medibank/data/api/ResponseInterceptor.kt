package com.medibank.data.api

import okhttp3.Interceptor
import okhttp3.Response

/**
 * The newsapi.org API rate limits free accounts to 100 api calls per day so we may hit a rate liit error quite easily.
 * This class converts that error code into an exception that can be handled in the view layers.
 */
class ResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 429)
            throw RateLimitException()

        return response
    }
}