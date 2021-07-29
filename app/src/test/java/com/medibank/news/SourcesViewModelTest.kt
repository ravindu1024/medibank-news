package com.medibank.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.medibank.data.api.RateLimitException
import com.medibank.data.models.domain.NewsSource
import com.medibank.data.usecases.sources.GetSourcesUseCase
import com.medibank.news.common.ViewState
import com.medibank.news.sources.SourcesViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SourcesViewModelTest {


    private val sourcesUsecase = mockk<GetSourcesUseCase>()
    private lateinit var viewModel: SourcesViewModel

    @Rule
    @JvmField
    var rx2OverrideRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup(){

        viewModel = SourcesViewModel(sourcesUsecase)
    }

    @Test
    fun `save source`(){
        val sourceSlot = slot<String>()
        every { sourcesUsecase.addOrRemoveSource(capture(sourceSlot)) } returns true

        viewModel.saveSource("test_source")
        Assert.assertEquals(sourceSlot.captured, "test_source")
    }

    @Test
    fun `get all sources - no sources`(){
        every { sourcesUsecase.getAllSources() } returns Single.just(emptyList())
        viewModel.getAllSources()

        Assert.assertEquals(viewModel.viewState.value, ViewState.DismissLoading)
        Assert.assertTrue(viewModel.sourcesList.value!!.isEmpty())

    }

    @Test
    fun `get all sources`(){
        val s1 = NewsSource("abc-news", "ABC", "test", true)
        val s2 = NewsSource("abc-news-au", "ABC AU", "test", false)
        every { sourcesUsecase.getAllSources() } returns Single.just(listOf(s1, s2))

        viewModel.getAllSources()

        Assert.assertEquals(viewModel.sourcesList.value!!.size, 2)
        Assert.assertEquals(viewModel.sourcesList.value!!.first().id, "abc-news")
        Assert.assertTrue(viewModel.sourcesList.value!!.first().saved)
        Assert.assertEquals(viewModel.viewState.value, ViewState.DismissLoading)
    }

    @Test
    fun `get all sources - error`(){
        every { sourcesUsecase.getAllSources() } returns Single.error(Exception())
        viewModel.getAllSources()

        val state = viewModel.viewState.value as ViewState.ShowError
        Assert.assertEquals(state.message, "Could not load sources")

    }

    @Test
    fun `get all sources - rate limited error`(){
        every { sourcesUsecase.getAllSources() } returns Single.error(RateLimitException())
        viewModel.getAllSources()

        val state = viewModel.viewState.value as ViewState.ShowError
        Assert.assertEquals(state.message, "Rate limited by API. Please try again in 12 hours.")

    }
}