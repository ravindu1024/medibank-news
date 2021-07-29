package com.medibank.news.detail

import androidx.lifecycle.ViewModel
import com.medibank.data.models.domain.NewsHeadline
import com.medibank.data.usecases.saveditems.SavedItemsUseCase

class NewsDetailViewModel(private val savedItemsUseCase: SavedItemsUseCase) : ViewModel() {

    fun addSavedHeadline(headline: NewsHeadline) {
        savedItemsUseCase.addSavedItem(headline)
    }

    fun removeSavedHeadline(headline: NewsHeadline) {
        savedItemsUseCase.removeSavedItem(headline)
    }

    fun isItemSaved(headline: NewsHeadline): Boolean {
        val saved = savedItemsUseCase.getSavedItems()
        return saved.firstOrNull { it.url == headline.url } != null
    }
}