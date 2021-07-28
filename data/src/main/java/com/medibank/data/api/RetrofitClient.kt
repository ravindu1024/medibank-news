package com.medibank.data.api

import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient() {
    companion object{

        fun init(baseUrl: String, requestInterceptor: RequestInterceptor): Retrofit{

            val intc = object: Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                    val response = chain.proceed(request)

                    if(response.code == 429)
                        throw RateLimitException()

                    return response
                }

            }

            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(requestInterceptor)
                .addInterceptor(intc)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}