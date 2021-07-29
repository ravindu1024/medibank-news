package com.medibank.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.medibank.data.api.RateLimitException
import com.medibank.data.models.domain.NewsHeadline
import com.medibank.data.usecases.news.GetNewsUseCase
import com.medibank.data.usecases.sources.GetSourcesUseCase
import com.medibank.news.common.ViewState
import com.medibank.news.headlines.HeadlineViewModel
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HeadlinesViewModelTest {

    private val newsUseCase = mockk<GetNewsUseCase>()
    private val sourcesUseCase = mockk<GetSourcesUseCase>()
    private lateinit var viewModel: HeadlineViewModel

    @Rule
    @JvmField
    var rx2OverrideRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup(){

        every { sourcesUseCase.getSavedSources() } returns listOf("abc-news", "al-jazeera")

        viewModel = HeadlineViewModel(newsUseCase, sourcesUseCase)
    }

    @Test
    fun `get headlines - no sources`(){
        every { sourcesUseCase.getSavedSources() } returns emptyList()
        viewModel.getHeadlines()

        val state = viewModel.viewState.value as ViewState.ShowError
        Assert.assertEquals(state.message, "Please select some sources to get headlines")
        Assert.assertTrue(viewModel.headlineList.value!!.isEmpty())
    }

    @Test
    fun `get headlines`(){
        val h1 = NewsHeadline("john doe", "ABC News", "title 1", "des", "url1", "image")
        val h2 = NewsHeadline("jane doe", "ABC News AU", "title 2", "des", "url2", "image")
        every { newsUseCase.getHeadlines() } returns Observable.just(listOf(h1, h2))

        viewModel.getHeadlines()

        Assert.assertEquals(viewModel.viewState.value, ViewState.DismissLoading)
        Assert.assertEquals(viewModel.headlineList.value!!.size, 2)
        Assert.assertEquals(viewModel.headlineList.value!!.first().author, "john doe")
    }

    @Test
    fun `get headlines - error`(){
        every { newsUseCase.getHeadlines() } returns Observable.error(Exception())
        viewModel.getHeadlines()

        val state = viewModel.viewState.value as ViewState.ShowError
        Assert.assertEquals(state.message, "Could not load headlines")

    }

    @Test
    fun `get headlines - rate limited error`(){
        every { newsUseCase.getHeadlines() } returns Observable.error(RateLimitException())
        viewModel.getHeadlines()

        val state = viewModel.viewState.value as ViewState.ShowError
        Assert.assertEquals(state.message, "Rate limited by API. Please try again in 12 hours.")

    }
}