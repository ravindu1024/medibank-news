package com.medibank.data.usecases.news

import android.util.Log
import com.medibank.data.api.NewsApi
import com.medibank.data.models.domain.NewsHeadline
import com.medibank.data.models.dto.NewsResponseDto
import com.medibank.data.models.dto.toDomain
import com.medibank.data.toCsv
import com.medibank.data.usecases.AppPreferences
import io.reactivex.Observable
import io.reactivex.functions.Function
import java.util.*

class GetNewsUseCase(private val api: NewsApi, private val preferences: AppPreferences) {

    fun getHeadlines(): Observable<List<NewsHeadline>> {
        val sources = preferences.getSavedSources()

        Log.d("NEWS", "Fetching news from sources: ${sources.toCsv()}")

        val observables = sources.map {
            api.getTopHeadlines(it)
        }

        return Observable.combineLatest(
            observables,
            Function { responses: Array<in NewsResponseDto> ->
                val allItems = mutableListOf<NewsHeadline>()
                responses.forEach { response ->
                    allItems.addAll((response as NewsResponseDto).toDomain())
                }
                return@Function allItems
            })
    }
}