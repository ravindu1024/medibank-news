package com.medibank.news.headlines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medibank.data.api.RateLimitException
import com.medibank.data.usecases.news.GetNewsUseCase
import com.medibank.data.usecases.sources.GetSourcesUseCase
import com.medibank.data.models.domain.NewsHeadline
import com.medibank.news.common.ViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HeadlineViewModel constructor(
    private val newsUseCase: GetNewsUseCase,
    private val sourcesUseCase: GetSourcesUseCase
) : ViewModel() {

    val headlineList = MutableLiveData<List<NewsHeadline>>()
    val viewState = MutableLiveData<ViewState>()
    private val compositeDisposable = CompositeDisposable()

    fun getHeadlines() {
        if (sourcesUseCase.getSavedSources().isEmpty()) {
            headlineList.value = emptyList()
            viewState.value = ViewState.ShowError("Please select some sources to get headlines")
            return
        }

        compositeDisposable.add(
            newsUseCase.getHeadlines()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.value = ViewState.ShowLoading
                }
                .subscribe({ headlines ->
                    viewState.value = ViewState.DismissLoading
                    headlineList.value = headlines
                }, {
                    if(it is RateLimitException)
                        viewState.value = ViewState.ShowError("Rate limited by API. Please try again in 12 hours.")
                    else
                        viewState.value = ViewState.ShowError("Could not load headlines")
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}