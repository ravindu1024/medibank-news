package com.medibank.data.usecases

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.medibank.data.models.domain.NewsHeadline

class AppPreferences(private val sp: SharedPreferences) {

    private val gson = Gson()

    companion object{
        const val PREF_SAVED_SOURCES = "saved_sources"
        const val PREF_SAVED_NEWS = "saved_news"
    }

    fun getSavedSources(): List<String>{
        val saved = sp.getString(PREF_SAVED_SOURCES, null)

        return if(saved == null)
            emptyList()
        else
            gson.fromJson(saved, Array<String>::class.java).toList()
    }

    fun addSavedSource(source: String){
        Log.d("NEWS", "add source: $source")
        val saved = getSavedSources()
        val newList = saved + listOf(source)

        sp.edit().putString(PREF_SAVED_SOURCES, gson.toJson(newList)).apply()
    }

    fun removeSource(source: String){
        Log.d("NEWS", "remove source: $source")
        val saved = getSavedSources()
        val newList = saved - listOf(source)

        sp.edit().putString(PREF_SAVED_SOURCES, gson.toJson(newList)).apply()
    }

    fun getSavedHeadlines(): List<NewsHeadline>{
        val saved = sp.getString(PREF_SAVED_NEWS, null)
        return if(saved == null)
            emptyList()
        else
            gson.fromJson(saved, Array<NewsHeadline>::class.java).toList()
    }

    fun addSavedHeadline(headline: NewsHeadline){
        val saved = getSavedHeadlines()
        if(saved.firstOrNull { it.url == headline.url } == null){
            val newList = saved + listOf(headline)
            sp.edit().putString(PREF_SAVED_NEWS, gson.toJson(newList)).apply()
        }
    }

    fun removeSavedHeadline(headline: NewsHeadline){
        val saved = getSavedHeadlines()
        val newList = saved.filter {
            it.url != headline.url
        }

        sp.edit().putString(PREF_SAVED_NEWS, gson.toJson(newList)).apply()
    }
}