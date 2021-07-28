package com.medibank.data.usecases.saveditems

import com.medibank.data.usecases.AppPreferences
import com.medibank.data.models.domain.NewsHeadline

class SavedItemsUseCase(private val prefs: AppPreferences) {

    fun addSavedItem(headline: NewsHeadline){
        prefs.addSavedHeadline(headline)
    }

    fun removeSavedItem(headline: NewsHeadline){
        prefs.removeSavedHeadline(headline)
    }

    fun getSavedItems(): List<NewsHeadline>{
        return prefs.getSavedHeadlines()
    }
}