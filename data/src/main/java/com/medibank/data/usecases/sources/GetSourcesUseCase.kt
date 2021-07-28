package com.medibank.data.usecases.sources

import android.util.Log
import com.medibank.data.api.NewsApi
import com.medibank.data.usecases.AppPreferences
import com.medibank.data.models.domain.NewsSource
import com.medibank.data.models.dto.toDomain
import com.medibank.data.toCsv
import io.reactivex.Single

class GetSourcesUseCase(private val api: NewsApi, private val prefs: AppPreferences){

    fun getAllSources(): Single<List<NewsSource>>{

        val saved = prefs.getSavedSources()
        Log.d("NEWS", "Saved sources: ${saved.toCsv()}")

        return api.getSources("en")
            .map { it.toDomain() }
            .map { sources ->
                sources.forEach { it.saved = saved.contains(it.id) }
                return@map sources
            }
    }

    fun getSavedSources(): List<String>{
        return prefs.getSavedSources()
    }

    fun addOrRemoveSource(source: String): Boolean{
        val saved = prefs.getSavedSources()

        return if(saved.contains(source)) {
            prefs.removeSource(source)
            false
        }else {
            prefs.addSavedSource(source)
            true
        }
    }
}