package com.medibank.news.sources

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medibank.data.api.RateLimitException
import com.medibank.data.models.domain.NewsSource
import com.medibank.data.usecases.sources.GetSourcesUseCase
import com.medibank.news.common.ViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SourcesViewModel(private val sourcesUseCase: GetSourcesUseCase) : ViewModel() {

    val sourcesList = MutableLiveData<List<NewsSource>>()
    val viewState = MutableLiveData<ViewState>()
    private val compositeDisposable = CompositeDisposable()

    fun getAllSources() {
        compositeDisposable.add(
            sourcesUseCase.getAllSources()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.value = ViewState.ShowLoading
                }
                .subscribe { sources, error ->
                    if (error == null) {
                        viewState.value = ViewState.DismissLoading
                        sourcesList.value = sources
                    } else {
                        if (error is RateLimitException)
                            viewState.value = ViewState.ShowError("Rate limited by API. Please try again in 12 hours.")
                        else
                            viewState.value = ViewState.ShowError("Could not load sources")
                    }
                }
        )
    }

    fun saveSource(source: String) {
        sourcesUseCase.addOrRemoveSource(source)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}