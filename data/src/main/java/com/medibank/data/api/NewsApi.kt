package com.medibank.data.api

import com.medibank.data.models.dto.NewsResponseDto
import com.medibank.data.models.dto.NewsSourceResponseDto
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines/sources")
    fun getSources(@Query("language") language: String): Single<NewsSourceResponseDto>

    @GET("/v2/top-headlines")
    fun getTopHeadlines(@Query("sources") sources: String): Observable<NewsResponseDto>
}