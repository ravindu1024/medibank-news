package com.medibank.news.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medibank.data.models.domain.NewsHeadline
import com.medibank.data.usecases.saveditems.SavedItemsUseCase
import com.medibank.news.common.ViewState

class SavedItemsViewModel(private val useCase: SavedItemsUseCase) : ViewModel() {

    val savedHeadlines = MutableLiveData<List<NewsHeadline>>()
    val viewState = MutableLiveData<ViewState>()

    fun loadSavedHeadlines() {
        val savedList = useCase.getSavedItems()
        if (savedList.isNotEmpty()) {
            savedHeadlines.value = savedList
            viewState.value = ViewState.DismissLoading
        } else {
            savedHeadlines.value = emptyList()
            viewState.value = ViewState.ShowError("No saved items")
        }
    }
}